package site.easy.to.build.crm.service.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.TicketStatusCount;
import site.easy.to.build.crm.repository.TicketStatutRepository;

import java.util.List;

@Service
public class TicketStatusService {

    private final TicketStatutRepository ticketStatutRepository;

    @Autowired
    public TicketStatusService(TicketStatutRepository ticketStatutRepository) {
        this.ticketStatutRepository = ticketStatutRepository;
    }

    // Méthode pour récupérer toutes les données de TicketStatusCount
    public List<TicketStatusCount> getAllTicketStatusCounts() {
        return ticketStatutRepository.findAll();
    }
}
