package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.entity.TicketStatusCount;
import site.easy.to.build.crm.service.dashboard.TicketStatusService;

import java.util.List;

@RestController
public class TicketStatusController {

    private final TicketStatusService ticketStatutService;

    @Autowired
    public TicketStatusController(TicketStatusService ticketStatutService) {
        this.ticketStatutService = ticketStatutService;
    }

    // Méthode pour obtenir la liste des statuts des tickets
    @GetMapping("/api/ticket-status/all")
    public List<TicketStatusCount> getAllTicketStatusCounts() {
        return ticketStatutService.getAllTicketStatusCounts();
    }
}
