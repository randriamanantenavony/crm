package site.easy.to.build.crm.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import site.easy.to.build.crm.service.depense.DepenseService;
import site.easy.to.build.crm.service.lead.MyLeadService;
import site.easy.to.build.crm.service.ticket.MyTicketService;

@RestController
@CrossOrigin(origins = "http://localhost:5077") // <- Appliqué globalement
public class DeleteController {

    private final DepenseService depenseService;
    private final MyLeadService myLeadService;
    private final MyTicketService myTicketService;

    public DeleteController(DepenseService depenseService, MyLeadService myLeadService, MyTicketService myTicketService) {
        this.depenseService = depenseService;
        this.myLeadService = myLeadService;
        this.myTicketService = myTicketService;
    }

    // Suppression par ticketId
    @DeleteMapping("api/delete_ticket/{id}")
    public ResponseEntity<String> deleteDepenseByTicketId(@PathVariable("id") Integer id) {
        System.out.println("coucou");
        myTicketService.deleteById(id);
        return ResponseEntity.ok("Dépense(s) supprimée(s) avec succès !");
    }

    @GetMapping("/api/delete_lead/{id}")
    public ResponseEntity<Void> deleteLeadAndRedirect(@PathVariable("id") Integer id, HttpServletResponse response) throws IOException {
        myLeadService.deleteById(id);  // Cette méthode supprimera le lead + dépenses automatiquement via cascade
        return ResponseEntity.ok().build();
    }


    @PutMapping("/api/update_ticket_montant")
    public ResponseEntity<String> updateMontant(@RequestParam("id") int id, @RequestParam("montant") double montant) {
        depenseService.updateMontantParTicket(id, montant);
        return ResponseEntity.ok("Montant mis à jour !");
    }

    @PutMapping("/api/update/lead/{leadId}")
    public ResponseEntity<String> updateMontantByLeadId(@PathVariable int leadId, @RequestParam double montant) {
        depenseService.updateMontantByLeadId(montant, leadId);
        return ResponseEntity.ok("Montant mis à jour avec succès pour le lead " + leadId);
    }

}
