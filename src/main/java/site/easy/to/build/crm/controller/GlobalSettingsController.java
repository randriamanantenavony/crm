package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.GlobalSettings;
import site.easy.to.build.crm.service.global.GlobalSettingsService;

@RestController
@RequestMapping("/api/settings")
public class GlobalSettingsController {
    
      @Autowired
    private GlobalSettingsService service;

    // @GetMapping("/update")
    // public GlobalSettings updateThreshold(@RequestParam double threshold) {
    //     System.out.println("coucou" + threshold);
    //     return service.updateThresholdSimple(threshold);
    // }    

        @CrossOrigin(origins = "*")
        @PutMapping("/update")
        public GlobalSettings updateThreshold(@RequestParam double threshold) {
            System.out.println("coucou " + threshold);
            return service.updateThresholdSimple(threshold);
        }


    @GetMapping("/{id}")
    public GlobalSettings getSettings(@PathVariable int id) {
        System.out.println(service.getSettings(id).getAlert_threshold());
        return service.getSettings(id);
    }
    
}
