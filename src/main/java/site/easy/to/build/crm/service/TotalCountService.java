package site.easy.to.build.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.TotalCount;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.repository.LeadRepository;
import site.easy.to.build.crm.repository.TicketRepository;

@Service
public class TotalCountService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public TotalCount getTotalCount() {
        TotalCount totalCount = new TotalCount();
        
        // Calculer le nombre de clients
        long customerCount = customerRepository.count();
        totalCount.setCustomerCount(customerCount);

        // Calculer le nombre de leads
        long leadCount = leadRepository.count();
        totalCount.setLeadCount(leadCount);

        // Calculer le nombre de tickets
        long ticketCount = ticketRepository.count();
        totalCount.setTicketCount(ticketCount);

        return totalCount;
    }
}
