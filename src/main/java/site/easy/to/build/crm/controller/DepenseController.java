package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.service.depense.DepenseService;
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

    // Afficher le formulaire
    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("depense", new Depense());
        model.addAttribute("leads", leadRepository.findAll());
        model.addAttribute("tickets", ticketRepository.findAll());
        return "depense/depense_form";
    }

    // Enregistrer une dépense
    @PostMapping("/save")
    public String saveDepense(@RequestParam String description,
                              @RequestParam(required = false) Double montant,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String dateDepense, 
                              @RequestParam(required = false) Integer ticketId,
                              @RequestParam(required = false) Integer leadId) {
    
        Depense depense = new Depense();
        depense.setDescription(description);
        depense.setMontant(montant != null ? montant : 0);
    
        // Convertir la date depuis String en java.sql.Date si dateDepense n'est pas null
        if (dateDepense != null && !dateDepense.isEmpty()) {
            depense.setDateDepense(Date.valueOf(dateDepense));  // Conversion explicite du String à Date
        }
    
        if (ticketId != null) {
            Ticket ticket = new Ticket();
            ticket.setTicketId(ticketId);
            depense.setTicket(ticket);
        }
    
        if (leadId != null) {
            Lead lead = new Lead();
            lead.setLeadId(leadId);
            depense.setLead(lead);
        }
    
        depenseService.save(depense);
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
