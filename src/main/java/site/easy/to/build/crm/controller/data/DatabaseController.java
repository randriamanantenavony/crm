package site.easy.to.build.crm.controller.data;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import site.easy.to.build.crm.service.data.DatabaseCleanupService;

@Controller
@RequestMapping("/admin/db")
public class DatabaseController {

    @Autowired
    private DatabaseCleanupService cleanupService;

    @GetMapping("/reinitialiser")
    public String showReinitialiserPage(Model model) {
        return "data/reset"; // Chemin dans templates/
    }

   @GetMapping("/truncate")
    public String truncateDatabase(Model model) {
        cleanupService.truncateAllTablesExceptExcluded();
        model.addAttribute("message", "Base de données nettoyée avec succès (sauf roles, users, user_roles).");
        return "data/reinitialiser"; // le fichier HTML à afficher
    }

}
