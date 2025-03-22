package site.easy.to.build.crm.controller.data;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import site.easy.to.build.crm.service.data.DynamicInsertService;

@Controller
@RequestMapping("/generate/data_managing")
public class GenerateDataController {
    
    private final DynamicInsertService dynamicInsertService;

    public GenerateDataController(DynamicInsertService dynamicInsertService) {
        this.dynamicInsertService = dynamicInsertService;
    }
    
    @GetMapping("/page")
    public String page(Model model) {
        return "data/generate";
    }

    @PostMapping("/generate")
    public String generateDate(@RequestParam String tableName,
                               @RequestParam int rows,
                               Model model) {
        try {
            dynamicInsertService.insertRandomData(tableName, rows);
            model.addAttribute("message", "Données générées avec succès pour la table " + tableName);
        }catch ( Exception e) {
            model.addAttribute("message", "Erreur : " + e.getMessage());
        }
        return "data/generate";
    }

}
