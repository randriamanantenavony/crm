package site.easy.to.build.crm.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.exceptions.CsvException;

import jakarta.transaction.Transactional;
import site.easy.to.build.crm.entity.CustomerBudget;
import site.easy.to.build.crm.entity.CustomerEntry;
import site.easy.to.build.crm.entity.ValidationError;
import site.easy.to.build.crm.service.importation.CsvImportService;
import site.easy.to.build.crm.entity.Customer;

@Controller
@RequestMapping("import/")
public class ImportController {
    
    @Autowired
    private CsvImportService csvImportService;


    @GetMapping("formulaire")
    public String showFomulaire(Model model){
        return "import";
    }

    @PostMapping("importer")
    public String importCsvFiles(@RequestParam("data1") MultipartFile data1,
                                 @RequestParam("data2") MultipartFile data2,
                                 @RequestParam("data3") MultipartFile data3, 
                                 Model model ) throws CsvException {
        try {
            // 1. Sauvegarder temporairement le fichier CSV
            String tempFilePath1 = saveFile(data1, "data1.csv");
            String tempFilePath2 = saveFile(data2, "data2.csv");
            String tempFilePath3 = saveFile(data3, "data3.csv");
    
            // 2. Appeler la méthode de validation directe
            List<ValidationError> validationErrors1 = csvImportService.validateCustomerInfoFile(tempFilePath1);
            List<ValidationError> validationErrors2 = csvImportService.importAndValidateErrorsOnly(tempFilePath2);
            List<ValidationError> validationErrors3 = csvImportService.validateLeadOrTicketFile(tempFilePath3);

            List<ValidationError> allErrors = new ArrayList<>();
            allErrors.addAll(validationErrors1);
            allErrors.addAll(validationErrors2);
            allErrors.addAll(validationErrors3);

            // 3. Ajouter les erreurs dans le modèle
            if (!allErrors.isEmpty()) {
            model.addAttribute("validationErrors", allErrors);
            model.addAttribute("message", "Des erreurs de validation ont été détectées.");
            } else {
                model.addAttribute("messageSuccess", "Les fichiers ont été importés et validés sans erreur !");
                List<Customer> c = csvImportService.readCustomersFromCsv(tempFilePath1);
                csvImportService.insertCustomers(c);
                List<CustomerBudget> customerBudgets = csvImportService.readCustomerBudgetFromCsv(tempFilePath2);
                csvImportService.insertBudget(customerBudgets);
                System.out.println("mama");
                List<CustomerEntry> entries = csvImportService.parseCSV(tempFilePath3);
                System.out.println("taille customerEntry : " + entries.size());
                csvImportService.processCustomerEntries(entries);
                System.out.println("tout va bien");
            }
            return "import";
        } catch (IOException | CsvException e) {
            model.addAttribute("message", "Erreur lors de l'importation : " + e.getMessage());
            return "import";
        }
    }
    
    
    private String saveFile(MultipartFile file, String fileName) throws IOException {
        // Sauvegarde temporaire du fichier
        String tempDir = System.getProperty("java.io.tmpdir");
        String filePath = tempDir + "/" + fileName;
        file.transferTo(new java.io.File(filePath));
        return filePath;
    }

}
