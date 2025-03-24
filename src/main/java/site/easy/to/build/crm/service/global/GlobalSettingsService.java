package site.easy.to.build.crm.service.global;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import site.easy.to.build.crm.entity.GlobalSettings;
import site.easy.to.build.crm.repository.GlobalSettingsRepository;

import java.util.Optional;

@Service
public class GlobalSettingsService {

    @Autowired
    private GlobalSettingsRepository repository;

    @Transactional
    public GlobalSettings updateThresholdSimple(double newThreshold) {
        System.out.println(">>> Mise à jour du seuil avec valeur : " + newThreshold);
    
        GlobalSettings settings = repository.findById(1)
                .orElseThrow(() -> new RuntimeException("Paramètre global introuvable"));
    
        settings.setAlert_threshold(newThreshold);
    
        repository.save(settings);
        repository.flush();
    
        return settings;
    }
    
    
    public GlobalSettings getSettings(int id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Settings not found"));
    }
}

