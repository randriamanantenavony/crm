package site.easy.to.build.crm.service.ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.TicketRepository;

@Service
public class MyTicketService {
    
    @Autowired
    private TicketRepository leadRepository;

    @Transactional
    public void deleteById(Integer idlead) {
        if (leadRepository.existsById(idlead)) {
            leadRepository.deleteById(idlead);
            System.out.println("Ticket supprimé avec ID: " + idlead);
        } else {
            throw new IllegalArgumentException("Aucun lead trouvé avec l'ID: " + idlead);
        }
    }
    


}

