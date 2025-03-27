package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.service.lead.LeadServiceImpl;
import site.easy.to.build.crm.service.ticket.TicketServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RestController
public class FileUploadController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TicketServiceImpl ticketRepository;

    @Autowired
    private LeadServiceImpl leadServiceImpl;

// @PostMapping("api/upload")
// public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
//     if (file.isEmpty()) {
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fichier vide.");
//     }

//     try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//         StringBuilder log = new StringBuilder();

//         String headerLine = reader.readLine(); // Ignorer la première ligne d'en-tête
//         String dataLine;

//         // Variables pour stocker le dernier client inséré
//         Customer currentCustomer = null;
//         Integer currentCustomerId = null;

//         while ((dataLine = reader.readLine()) != null) {
//             log.append("Ligne reçue : ").append(dataLine).append("\n");

//             // Parser la ligne de données
//             String[] values = dataLine.split(",");

//             // Si ligne contient les données client (au moins 5 colonnes), alors nouveau client
//             if (values.length >= 5) {
//                 Customer customer = new Customer();
//                 customer.setName(values[0].trim());
//                 customer.setEmail(values[1].trim());
//                 customer.setPosition(values[2].trim());
//                 customer.setPhone(values[3].trim());
//                 customer.setAddress(values[4].trim());

//                 Customer savedCustomer = customerRepository.save(customer);
//                 currentCustomer = savedCustomer;
//                 currentCustomerId = savedCustomer.getCustomerId();

//                 log.append("Client inséré en base avec ID : ").append(currentCustomerId).append("\n");
//             }

//             // Ligne de ticket : au moins 4 colonnes requises
//             if (values.length >= 4 && currentCustomer != null) {
//                 Ticket ticket = new Ticket();
//                 ticket.setSubject(values[0].trim());
//                 ticket.setDescription(values[1].trim());
//                 ticket.setStatus(values[2].trim());
//                 ticket.setPriority(values[3].trim());

//                 ticket.setCustomer(currentCustomer);

//                 ticketRepository.save(ticket);
//                 log.append("Ticket inséré en base lié au client ID ").append(currentCustomerId).append(" : ").append(values[0]).append("\n");
//             } else if (values.length >= 4 && currentCustomer == null) {
//                 log.append("Aucun client associé trouvé pour ce ticket, ligne ignorée.\n");
//             } else {
//                 log.append("Données incomplètes pour insertion.\n");
//             }
//         }

//         return ResponseEntity.ok("Succès :\n" + log.toString());

//     } catch (IOException e) {
//         e.printStackTrace();
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                 .body("Erreur lors du traitement : " + e.getMessage());
//     }
// }

@PostMapping("api/upload")
public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
    if (file.isEmpty()) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fichier vide.");
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
        StringBuilder log = new StringBuilder();

        String headerLine = reader.readLine(); // 1ère ligne (headers)
        String dataLine;

        // Variables pour stocker l'état du client courant
        Customer currentCustomer = null;
        Integer currentCustomerId = null;

        // Ignorer les 2 premières lignes d'en-tête et données clients
        while ((dataLine = reader.readLine()) != null) {
            log.append("Ligne reçue : ").append(dataLine).append("\n");

            // Parser la ligne de données
            String[] values = dataLine.split(",");

            // Si la ligne contient des données pour un client (5 valeurs attendues)
            if (values.length >= 5) {
                // Créer et insérer un client
                Customer customer = new Customer();
                customer.setName(values[0].trim());
                customer.setEmail(values[1].trim());
                customer.setPosition(values[2].trim());
                customer.setPhone(values[3].trim());
                customer.setAddress(values[4].trim());

                // Sauvegarder le client et récupérer l'ID généré
                Customer savedCustomer = customerRepository.save(customer);
                currentCustomer = savedCustomer;
                currentCustomerId = savedCustomer.getCustomerId();

                log.append("Client inséré en base avec ID : ").append(currentCustomerId).append("\n");
            }

            // Si la ligne contient un ticket (4 valeurs attendues)
            if (values.length >= 4 && currentCustomer != null) {
                Ticket ticket = new Ticket();
                ticket.setSubject(values[0].trim());
                ticket.setDescription(values[1].trim());
                ticket.setStatus(values[2].trim());
                ticket.setPriority(values[3].trim());
                ticket.setCustomer(currentCustomer);  // Lier le ticket au client

                ticketRepository.save(ticket);
                log.append("Ticket inséré en base : ").append(values[0]).append("\n");
            }

            // Si la ligne contient des données pour un lead (3 valeurs attendues)
            if (values.length == 3 && currentCustomer != null) {
                Lead lead = new Lead();
                lead.setName(values[0].trim());
                lead.setStatus(values[1].trim());
                lead.setPhone(values[2].trim());
                lead.setCustomer(currentCustomer); // Lier le lead au client

                // Utilisation de LeadServiceImpl pour sauvegarder le lead
                leadServiceImpl.save(lead);
                log.append("Lead inséré en base : ").append(values[0]).append("\n");
            }
        }

        return ResponseEntity.ok("Succès :\n" + log.toString());

    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erreur lors du traitement : " + e.getMessage());
    }
}



}
