package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.UserBudgetAlert;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.depense.UserBudgetAlertService;
import site.easy.to.build.crm.service.lead.LeadServiceImpl;
import site.easy.to.build.crm.service.ticket.TicketServiceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/depense")
public class DepenseController {

    @Autowired
    private DepenseService depenseService;

    @Autowired
    private LeadServiceImpl leadRepository;

    @Autowired
    private TicketServiceImpl ticketRepository;

    @Autowired
    private UserBudgetAlertService userBudgetAlertService;

    // Afficher le formulaire
    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("depense", new Depense());
        model.addAttribute("leads", leadRepository.findAll());
        model.addAttribute("tickets", ticketRepository.findAll());
        return "depense/depense_form";
    }

    @PostMapping("/save")
    public String saveDepense(@RequestParam String description,
                              @RequestParam(required = false) Double montant,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String dateDepense, 
                              @RequestParam(required = false) Integer ticketId,
                              @RequestParam(required = false) Integer leadId,
                              @RequestParam(required = false) String confirmDepense, // Pour vérifier la confirmation de l'utilisateur
                              Model model,
                              RedirectAttributes redirectAttributes) {
    
        System.out.println("ticket id :" + ticketId);
        System.out.println("lead id :" + leadId);
        Depense depense = new Depense();
        depense.setDescription(description);
        depense.setMontant(montant != null ? montant : 0);
        Customer customer = null;
        Ticket ticket = null;
        Lead lead = null;
    
        // Convertir la date depuis String en java.sql.Date si dateDepense n'est pas null
        if (dateDepense != null && !dateDepense.isEmpty()) {
            depense.setDateDepense(Date.valueOf(dateDepense));  // Conversion explicite du String à Date
        }
    
        // Récupérer le Customer via le Lead ou le Ticket
        if (ticketId != null) {
            ticket = ticketRepository.findByTicketId(ticketId);  // Récupérer le Ticket avec son ID
            if (ticket != null) {
                customer = ticket.getCustomer();  // Récupérer le Customer associé au Ticket
            }
        }
    
        if (leadId != null) {
            lead = leadRepository.findByLeadId(leadId);  // Récupérer le Lead avec son ID
            if (lead != null) {
                customer = lead.getCustomer();  // Récupérer le Customer associé au Lead
            }
        }

        depense.setLead(lead);
        depense.setTicket(ticket);
    
        // Sauvegarder la dépense si l'utilisateur a confirmé ou si le budget est respecté
        depenseService.save(depense);

        System.out.println("customer id : " + customer.getCustomerId());
        // Obtenir l'alerte du budget de l'utilisateur
        UserBudgetAlert alert = userBudgetAlertService.getUserBudgetAlert(customer.getCustomerId());

        System.out.println("total depense : " + alert.getTotalDepense());
        System.out.println("total budget : " + alert.getTotalBudget());
        System.out.println("seuil : " + alert.getAlertThresholdValue());
        System.out.println("resultat : " + alert.getTotalDepense().compareTo(alert.getAlertThresholdValue()));
        System.out.println("signe final : " + alert.getAlertTriggered());

          if (alert != null && alert.getTotalDepense().compareTo(alert.getAlertThresholdValue()) > 0) {
        System.out.println("seuil depasse");
        redirectAttributes.addFlashAttribute("budgetAlert", alert);  
        
       } 
       
        redirectAttributes.addFlashAttribute("seuilDepasse", true);

        // Rediriger vers la liste des dépenses
        return "redirect:/depense/list";
    }
        

    // Afficher toutes les dépenses
    @GetMapping("/list")
    public String listDepenses(Model model) {
        List<Depense> depenses = depenseService.getAll();
        model.addAttribute("depenses", depenses);
        return "depense/depense_list";
    }
}
