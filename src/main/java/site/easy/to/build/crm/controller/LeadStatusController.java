package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.LeadStatusCount;
import site.easy.to.build.crm.entity.TicketStatusCount;
import site.easy.to.build.crm.service.dashboard.LeadStatusService;
import site.easy.to.build.crm.service.dashboard.TicketStatusService;

import java.util.List;

@RestController
public class LeadStatusController {

    private final LeadStatusService leadStatutService;

    @Autowired
    public LeadStatusController(LeadStatusService leadStatutService) {
        this.leadStatutService = leadStatutService;
    }

    // Méthode pour obtenir la liste des statuts des tickets
    @GetMapping("/api/lead-status/all")
    public List<LeadStatusCount> getAllTicketStatusCounts() {
        return leadStatutService.getAllLeadStatusCounts();
    }
}
