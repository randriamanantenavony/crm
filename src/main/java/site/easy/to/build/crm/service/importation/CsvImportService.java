package site.easy.to.build.crm.service.importation;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerBudget;
import site.easy.to.build.crm.entity.CustomerEntry;
import site.easy.to.build.crm.entity.TempBudgetInfo;
import site.easy.to.build.crm.entity.TempCustomerInfo;
import site.easy.to.build.crm.entity.TempLeadTicket;
import site.easy.to.build.crm.entity.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class CsvImportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ValidationError> importAndValidateErrorsOnly(String filePath) throws IOException, CsvException {
        List<ValidationError> errors = new ArrayList<>();
    
        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> rows = reader.readAll();
    
        if (rows.isEmpty()) {
            errors.add(new ValidationError(0, "temp_budget_info", "Fichier CSV vide"));
            return errors;
        }
    
        // En-tête
        rows.remove(0);
    
        // Création de la table temporaire
        jdbcTemplate.execute("DROP TEMPORARY TABLE IF EXISTS temp_budget_info");
        jdbcTemplate.execute("""
            CREATE TEMPORARY TABLE temp_budget_info (
                customer_email VARCHAR(255),
                budget DECIMAL(18, 2)
            )
        """);
    
        String insertQuery = "INSERT INTO temp_budget_info (customer_email, budget) VALUES (?, ?)";
    
        int line = 1; // Ligne 1 = en-tête, donc données commencent à 2
        for (String[] row : rows) {
            line++;
            try {
                String email = row[0].trim();
                String budgetStr = row[1].trim().replace("\"", "").replace(",", "");
                BigDecimal budget = new BigDecimal(budgetStr);
    
                jdbcTemplate.update(insertQuery, email, budget);
            } catch (Exception e) {
                errors.add(new ValidationError(line, "temp_budget_info", "Erreur d'insertion : " + e.getMessage()));
            }
        }
    
        List<TempBudgetInfo> records = jdbcTemplate.query("SELECT * FROM temp_budget_info",
            (rs, rowNum) -> {
                TempBudgetInfo b = new TempBudgetInfo();
                b.setCustomerEmail(rs.getString("customer_email"));
                b.setBudget(rs.getBigDecimal("budget"));
                return b;
            });
    
        int rowNumber = 2;
        for (TempBudgetInfo record : records) {
            if (record.getCustomerEmail() == null || !record.getCustomerEmail().contains("@")) {
                errors.add(new ValidationError(rowNumber, "temp_budget_info", "Email invalide"));
            }
            if (record.getBudget() == null || record.getBudget().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add(new ValidationError(rowNumber, "temp_budget_info", "Budget doit être > 0"));
            }
            rowNumber++;
        }
    
        return errors;
    }
    
    public List<ValidationError> validateCustomerInfoFile(String filePath) throws IOException, CsvException {
        List<ValidationError> errors = new ArrayList<>();
        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> rows = reader.readAll();
    
        String tableName = "temp_customer_info";
    
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            int rowNumber = i + 1;
    
            if (row.length < 2) {
                errors.add(new ValidationError(rowNumber, tableName, "Colonnes manquantes"));
                continue;
            }
    
            String email = row[0].trim();
            String name = row[1].trim();
    
            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                errors.add(new ValidationError(rowNumber, tableName, "Email invalide: " + email));
            }
    
            if (name.isEmpty()) {
                errors.add(new ValidationError(rowNumber, tableName, "Nom du client vide"));
            }
        }
    
        return errors;
    }
    

    public List<ValidationError> validateLeadOrTicketFile(String filePath) {
        List<ValidationError> errors = new ArrayList<>();
        String tableName = "temp_lead_ticket";
    
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> lines = reader.readAll();
    
            for (int i = 1; i < lines.size(); i++) { // On saute l'en-tête
                String[] row = lines.get(i);
                int lineNumber = i + 1; // ligne réelle dans le fichier
    
                System.out.println("Ligne " + lineNumber + ": " + Arrays.toString(row));
                
                if (row.length < 5) { // Si la ligne n'a pas au moins 5 colonnes (email, subject, type, status, expense)
                errors.add(new ValidationError(lineNumber, tableName, "Ligne mal formatée, nombre de colonnes insuffisant"));
                continue; // Passer à la ligne suivante
               }

                String email = row[0].trim();
                String subject = row[1].trim();
                String type = row[2].trim().toLowerCase();
                String status = row[3].trim().toLowerCase();
                String expenseRaw = row[4].trim().replace(",", ".").replaceAll("[^\\d.-]", "");
    
                // Email valide
                if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                    errors.add(new ValidationError(lineNumber, tableName, "Email invalide"));
                }
    
                // Sujet/nom obligatoire
                if (subject.isEmpty()) {
                    errors.add(new ValidationError(lineNumber, tableName, "Le champ sujet/nom est vide"));
                }
    
                // Type valide
                if (!type.equals("lead") && !type.equals("ticket")) {
                    errors.add(new ValidationError(lineNumber, tableName, "Type doit être 'lead' ou 'ticket'"));
                }
    
                // Validation de status
                if (status.isEmpty()) {
                    errors.add(new ValidationError(lineNumber, tableName, "Le champ statut est vide"));
                }
    
                // Dépense > 0
                try {
                    double expense = Double.parseDouble(expenseRaw);
                    if (expense <= 0) {
                        errors.add(new ValidationError(lineNumber, tableName, "La dépense doit être > 0"));
                    }
                } catch (NumberFormatException e) {
                    errors.add(new ValidationError(lineNumber, tableName, "Valeur de dépense invalide"));
                }
            }
    
        } catch (IOException | CsvException e) {
            errors.add(new ValidationError(0, tableName, "Erreur de lecture du fichier : " + e.getMessage()));
        }
    
        return errors;
    }
    
    public List<Customer> readCustomersFromCsv(String filePath) {
        List<Customer> customerList = new ArrayList<>();
        Random random = new Random(); // Pour générer des données aléatoires

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            
            // Passer l'en-tête
            csvReader.readNext();
            
            // Lire chaque ligne du CSV
            while ((nextLine = csvReader.readNext()) != null) {
                // nextLine[0] est le email et nextLine[1] est le nom
                String email = nextLine[0];
                String name = nextLine[1];

                // Génération de données aléatoires pour les autres champs
                String phone = "01" + (random.nextInt(900000000) + 600000000); // Génère un numéro de téléphone aléatoire
                String address = "Address " + random.nextInt(1000); // Génère une adresse aléatoire
                String city = "City " + random.nextInt(100); // Génère une ville aléatoire
                String state = "State " + random.nextInt(50); // Génère un état aléatoire
                String country = "Country " + random.nextInt(20); // Génère un pays aléatoire
                String description = "Description for " + name;
                String position = "Position " + random.nextInt(10); // Génère une position aléatoire
                String twitter = "https://twitter.com/" + name.replace(" ", "_").toLowerCase();
                String facebook = "https://facebook.com/" + name.replace(" ", "_").toLowerCase();
                String youtube = "https://youtube.com/" + name.replace(" ", "_").toLowerCase();
                LocalDateTime createdAt = LocalDateTime.now();

                // Créer un objet Customer avec ces données
                Customer customer = new Customer(name, email, phone, address, city, state, country,
                        description, position, twitter, facebook, youtube, createdAt);
                
                // Ajouter l'objet Customer à la liste
                customerList.add(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        
        return customerList;
    }
    
    public void insertCustomers(List<Customer> customers) {
        String sql = "INSERT INTO customer (name, email, phone, address, city, state, country, description, position, twitter, facebook, youtube, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        for (Customer customer : customers) {
            jdbcTemplate.update(sql, 
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getCity(),
                customer.getState(),
                customer.getCountry(),
                customer.getDescription(),
                customer.getPosition(),
                customer.getTwitter(),
                customer.getFacebook(),
                customer.getYoutube(),
                customer.getCreatedAt());
        }
    }
    

     public List<CustomerBudget> readCustomerBudgetFromCsv(String filePath) {
        List<CustomerBudget> customerBudgetList = new ArrayList<>();
        
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] nextLine;
            
            // Passer l'en-tête
            csvReader.readNext();
            
            // Lire chaque ligne du CSV
            while ((nextLine = csvReader.readNext()) != null) {
                // nextLine[0] est le email et nextLine[1] est le budget
                String email = nextLine[0];
                String budgetStr = nextLine[1];
                
                // Convertir le budget en double
                double budget = parseBudget(budgetStr);
                
                // Créer un objet CustomerBudget et l'ajouter à la liste
                customerBudgetList.add(new CustomerBudget(email, budget));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }
        
        return customerBudgetList;
    }

    private double parseBudget(String budgetStr) {
        try {
            return Double.parseDouble(budgetStr);
        } catch (NumberFormatException e) {
            // Si le budget n'est pas valide, retourner 0
            System.out.println("Erreur de format pour le budget: " + budgetStr);
            return 0;
        }
    }

    @Transactional(rollbackFor = Exception.class)  // Spécifie que toute exception ou erreur entraînera un rollback
    public void insertBudget(List<CustomerBudget> customerBudgetList) {
        String selectCustomerIdQuery = "SELECT customer_id FROM customer WHERE email = ?";
        String insertBudgetQuery = "INSERT INTO budget (customer_id, name, total_amount, date_ajout) VALUES (?, ?, ?, NOW())";
        
        for (CustomerBudget customerBudget : customerBudgetList) {
            String email = customerBudget.getEmail();
            double budget = customerBudget.getBudget();
            
            // Message de débogage pour afficher l'email du client en cours
            System.out.println("Traitement de l'email : " + email);
            
            // Récupérer le customer_id en fonction de l'email
            Integer customerId = jdbcTemplate.queryForObject(selectCustomerIdQuery, Integer.class, email);
            
            // Vérifier si customerId est null, dans ce cas on annule tout et retourne sans rien
            if (customerId == null) {
                // Si aucun client trouvé, on annule toute la transaction et on sort de la méthode
                System.out.println("Aucun client trouvé pour l'email : " + email);
                return;  // Retourner sans rien insérer
            }
            
            // Si customerId n'est pas null, procéder à l'insertion
            String budgetName = "Budget for " + email;
            jdbcTemplate.update(insertBudgetQuery, customerId, budgetName, budget);
            
            // Message de débogage pour confirmer l'insertion
            System.out.println("Budget inséré pour " + email + " avec le montant de " + budget);
        }
    }


    public List<CustomerEntry> parseCSV(String filePath) {
        System.out.println("Methode parseCsv avec chemin de fichier");
        List<CustomerEntry> entries = new ArrayList<>();
        
        // Utiliser un try-with-resources pour garantir que le fichier est bien fermé après traitement
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            String[] line;
            boolean isFirstLine = true;

            while ((line = reader.readNext()) != null) {
                System.out.println("Processing line: " + String.join(",", line));

                // Ignorer la première ligne (en-tête)
                if (isFirstLine) {
                    isFirstLine = false;
                    System.out.println("Skipping header line.");
                    continue;
                }

                // Vérifier si le nombre de colonnes est suffisant
                if (line.length < 5) {
                    System.out.println("Line skipped due to insufficient fields: " + String.join(",", line));
                    continue;
                }

                // Extraire et traiter les données
                String email = line[0].trim();
                String subject = line[1].trim();
                String type = line[2].trim();
                String status = line[3].trim();
                String rawExpense = line[4].trim().replace("\"", "").replace(",", ".");

                System.out.println("Parsed values - Email: " + email + ", Subject: " + subject + ", Type: " + type + ", Status: " + status + ", RawExpense: " + rawExpense);

                try {
                    double expense = Double.parseDouble(rawExpense);
                    System.out.println("Parsed expense: " + expense);
                    entries.add(new CustomerEntry(email, subject, type, status, expense));
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing expense: " + rawExpense);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            e.printStackTrace();
        } catch (CsvValidationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

        return entries;
    }

    // Gère les champs CSV avec ou sans guillemets et virgules
    private static List<String> parseCSVLine(String line) {
        System.out.println("coucou");
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
    
        System.out.println("Processing line: " + line);  // Afficher la ligne traitée
    
        for (char c : line.toCharArray()) {
            System.out.println("Current char: " + c);  // Afficher chaque caractère de la ligne
    
            if (c == '\"') {
                inQuotes = !inQuotes;  // Toggle inQuotes flag
                System.out.println("Toggled quotes. inQuotes: " + inQuotes);
            } else if (c == ',' && !inQuotes) {
                tokens.add(current.toString().trim());  // Add the current field to tokens
                System.out.println("Field added: " + current.toString().trim());
                current.setLength(0);  // Reset the StringBuilder for the next field
            } else {
                current.append(c);  // Accumulate characters for the current field
            }
        }
        
        // Ajouter le dernier token (si existe) et afficher ce qui est ajouté
        tokens.add(current.toString().trim());
        System.out.println("Final token added: " + current.toString().trim());
    
        // Vérification si la dernière colonne (expense) contient des guillemets
        if (tokens.size() > 4) {
            String rawExpense = tokens.get(4).replace("\"", "");  // Remove the quotes if present
            System.out.println("Raw expense value: " + rawExpense);  // Afficher la valeur brute de l'expense
            rawExpense = rawExpense.replace(",", ".");  // Replace commas with dots for decimal numbers
            System.out.println("Processed expense value: " + rawExpense);  // Afficher la valeur traitée
    
            try {
                double expense = Double.parseDouble(rawExpense);  // Convert the expense to double
                tokens.set(4, String.valueOf(expense));  // Update the expense field with the double value
                System.out.println("Converted expense value: " + expense);  // Afficher la valeur convertie
            } catch (NumberFormatException e) {
                System.out.println("Erreur lors de la conversion de l'expense: " + rawExpense);
            }
        }
    
        // Afficher le résultat final avant le retour
        System.out.println("Tokens after processing: " + tokens);
    
        return tokens;
    }
    

    
     public void processCustomerEntries(List<CustomerEntry> entries) {
        for (CustomerEntry entry : entries) {
            // 1. Vérifier existence du customer
            Integer customerId = getCustomerIdByEmail(entry.getCustomerEmail());
            if (customerId == null) {
                System.out.println("Client non trouvé : " + entry.getCustomerEmail());
                return;
            }

            // 2. Conversion du montant
            double montant;
            try {
                montant = parseExpense(String.valueOf(entry.getExpense()));
            } catch (Exception e) {
                System.out.println("Montant invalide : " + entry.getExpense());
                return;
            }

            Integer leadId = null;
            Integer ticketId = null;

            if ("lead".equalsIgnoreCase(entry.getType())) {
                leadId = insertLead(entry.getSubjectOrName(), entry.getStatus(), customerId);
            } else if ("ticket".equalsIgnoreCase(entry.getType())) {
                ticketId = insertTicket(entry.getSubjectOrName(), entry.getStatus(), customerId);
            } else {
                System.out.println("Type inconnu : " + entry.getType());
                continue;
            }

            // 3. Insérer dans depense
            insertDepense(entry.getSubjectOrName(), montant, leadId, ticketId);
        }
    }

    private Integer getCustomerIdByEmail(String email) {
        String sql = "SELECT customer_id FROM customer WHERE email = ?";
        List<Integer> result = jdbcTemplate.query(sql, new Object[]{email}, (rs, rowNum) -> rs.getInt("customer_id"));
        return result.isEmpty() ? null : result.get(0);
    }

    private Integer insertLead(String name, String status, Integer customerId) {
        String sql = "INSERT INTO trigger_lead (name, status, customer_id, user_id, phone, employee_id) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
    
        // Valeurs fictives / par défaut
        int userId = 54; // valeur par défaut ou fixe
        String phone = "0382099540";
        int employeId = 56; // par exemple, employé par défaut
    
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, status);
            ps.setInt(3, customerId);
            ps.setInt(4, userId);
            ps.setString(5, phone);
            ps.setInt(6, employeId);
            return ps;
        }, keyHolder);
    
        return keyHolder.getKey().intValue();
    }
    

    private Integer insertTicket(String subject, String statut, Integer customerId) {
        String sql = "INSERT INTO trigger_ticket (subject, status, customer_id, manager_id, priority, employee_id) VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
    
        // Valeurs générées manuellement
        int managerId = 56; // valeur par défaut fictive
        String priority = "medium"; // valeur par défaut
        int employeId = 56; // valeur fictive
    
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, subject);
            ps.setString(2, statut);
            ps.setInt(3, customerId);
            ps.setInt(4, managerId);
            ps.setString(5, priority);
            ps.setInt(6, employeId);
            return ps;
        }, keyHolder);
    
        return keyHolder.getKey().intValue();
    }
    


    private void insertDepense(String description, double montant, Integer leadId, Integer ticketId) {
        String sql = "INSERT INTO depense (description, montant, lead_id, ticket_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, description, montant, leadId, ticketId);
    }

    private double parseExpense(String raw) {
        raw = raw.replace("\"", "").replace(",", ".").trim();
        return Double.parseDouble(raw);
    }

}
