package site.easy.to.build.crm.service.lead;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.easy.to.build.crm.repository.LeadRepository;

@Service
public class MyLeadService {
    
    @Autowired
    private LeadRepository leadRepository;

    @Transactional
    public void deleteById(Integer idlead) {
        if (leadRepository.existsById(idlead)) {
            leadRepository.deleteById(idlead);
            System.out.println("Lead supprimé avec ID: " + idlead);
        } else {
            throw new IllegalArgumentException("Aucun lead trouvé avec l'ID: " + idlead);
        }
    }
   
}
