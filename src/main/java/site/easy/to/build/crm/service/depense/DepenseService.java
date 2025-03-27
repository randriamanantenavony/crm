package site.easy.to.build.crm.service.depense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.easy.to.build.crm.entity.Depense;
import site.easy.to.build.crm.repository.DepenseRepository;

import java.util.List;

@Service
public class DepenseService {

    private final DepenseRepository depenseRepository;

    @Autowired
    public DepenseService(DepenseRepository depenseRepository) {
        this.depenseRepository = depenseRepository;
    }

    public Depense save(Depense depense) {
        return depenseRepository.save(depense);
    }

    public List<Depense> getAll() {
        return depenseRepository.findAll();
    }
    
    @Transactional
    public void delete(Depense depense) {
        if (depense != null && depenseRepository.existsById(depense.getId())) {
            depenseRepository.delete(depense);
            System.out.println("Dépense supprimée : " + depense.getId());
    
            // Vérification que la suppression a bien eu lieu
            if (!depenseRepository.existsById(depense.getId())) {
                System.out.println("Dépense effectivement supprimée.");
            }
        } else {
            System.out.println("Dépense introuvable pour la suppression.");
        }
    }
    
    public void deleteDepense(Integer depenseId) {
        depenseRepository.deleteById(depenseId);
    }

    @Transactional
    public void deleteDepenseByTicketId(Integer ticketId) {
        depenseRepository.deleteByTicketId(ticketId);
    } 

    @Transactional
    public void deleteDepenseByLeadId(Integer ticketId) {
        depenseRepository.deleteByLeadId(ticketId);
    } 

    public void updateMontantParTicket(int ticketId, double montant) {
        depenseRepository.updateMontantByTicketId(montant, ticketId);
    }

    public void updateMontantByLeadId(double montant, int leadId) {
        depenseRepository.updateMontantByLeadId(montant, leadId);
    }
    
}

