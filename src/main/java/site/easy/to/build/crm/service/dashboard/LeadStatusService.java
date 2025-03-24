package site.easy.to.build.crm.service.dashboard;

import java.util.List;

import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.LeadStatusCount;
import site.easy.to.build.crm.entity.TicketStatusCount;
import site.easy.to.build.crm.repository.LeadStatusRepository;

@Service
public class LeadStatusService {
    
    private final LeadStatusRepository leadStatusRepository;

    public LeadStatusService(LeadStatusRepository leadStatusRepository) {
        this.leadStatusRepository = leadStatusRepository;
    }

     public List<LeadStatusCount> getAllLeadStatusCounts() {
        return leadStatusRepository.findAll();
    }

}
