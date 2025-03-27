CREATE TABLE IF NOT EXISTS GlobalSettings (
    setting_id INT AUTO_INCREMENT PRIMARY KEY,
    alert_threshold DECIMAL(5, 2) NOT NULL
);

INSERT INTO GlobalSettings (alert_threshold) VALUES (80.00);


CREATE TABLE IF NOT EXISTS Budget (
    budget_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT UNSIGNED NOT NULL,  -- Changé en INT UNSIGNED
    name VARCHAR(100) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    date_ajout DATETIME NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS `depense` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `montant` DECIMAL(10,2) NOT NULL,
  `description` TEXT,
  `date_depense` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `lead_id` INT UNSIGNED DEFAULT NULL,
  `ticket_id` INT UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_depense_lead` (`lead_id`),
  KEY `fk_depense_ticket` (`ticket_id`),
  CONSTRAINT `fk_depense_lead` FOREIGN KEY (`lead_id`) REFERENCES `trigger_lead` (`lead_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_depense_ticket` FOREIGN KEY (`ticket_id`) REFERENCES `trigger_ticket` (`ticket_id`) ON DELETE CASCADE,
  CONSTRAINT `chk_one_reference_only` CHECK (
    (lead_id IS NOT NULL AND ticket_id IS NULL) OR
    (lead_id IS NULL AND ticket_id IS NOT NULL)
  )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- cree une vue pour l'alerte
CREATE OR REPLACE VIEW user_budget_alert AS
SELECT 
    c.customer_id AS userId,
    IFNULL(b.total_budget, 0) AS totalBudget,
    IFNULL(d.total_depense, 0) AS totalDepense,
    (IFNULL(b.total_budget, 0) * gs.alert_threshold / 100) AS alertThresholdValue,
    (IFNULL(d.total_depense, 0) > (IFNULL(b.total_budget, 0) * gs.alert_threshold / 100)) AS alertTriggered
FROM 
    customer c
LEFT JOIN (
    SELECT customer_id, SUM(total_amount) AS total_budget
    FROM budget
    GROUP BY customer_id
) b ON c.customer_id = b.customer_id
LEFT JOIN (
    SELECT 
        t.customer_id,
        SUM(dep.montant) AS total_depense
    FROM (
        SELECT customer_id, lead_id AS id, 'lead' AS type FROM trigger_lead
        UNION ALL
        SELECT customer_id, ticket_id AS id, 'ticket' AS type FROM trigger_ticket
    ) t
    JOIN depense dep 
        ON ( (t.type = 'lead' AND dep.lead_id = t.id) 
          OR (t.type = 'ticket' AND dep.ticket_id = t.id) )
    GROUP BY t.customer_id
) d ON c.customer_id = d.customer_id
CROSS JOIN GlobalSettings gs;


create or REPLACE view client_stat as
select u.userId as user_id,name,totalBudget as total_budget, totalDepense as total_depense
from user_budget_alert u
JOIN customer ON u.userId = customer_id;

CREATE VIEW ticket_status_counts AS
SELECT status, COUNT(*) AS ticket_count
FROM trigger_ticket
GROUP BY status;


CREATE OR REPLACE  VIEW lead_status_counts AS
SELECT status, COUNT(*) AS lead_count
FROM trigger_lead
GROUP BY status;

create or replace view v_detail_budget as 
select budget_id as budgetId,c.customer_id as customerId,c.name,total_amount as totalAmount,date_ajout as dateAjout,email as customerEmail
from budget b 
join customer c on b.customer_id = c.customer_id;


create or replace view v_total as
 select sum(totatl_amount) from budget


select count(*) from customer;
select count(*) from trigger_lead;
select count(*) from trigger_ticket;


create or replace view v_total as 
select sum(total_amount) as total_budget from budget group by customer_id,
select sum(montant) as somme _depense from depense;




-- 1. Insérer un Lead
INSERT INTO trigger_lead (customer_id, user_id, name, phone, employee_id, status, meeting_id, google_drive, google_drive_folder_id, created_at)
VALUES (1, 2, 'John Doe', '123-456-7890', 3, 'Active', 'meeting123', 1, 'folder123', NOW());

-- 2. Insérer une Depense pour le Lead récemment inséré
INSERT INTO depense (montant, description, date_depense, lead_id)
VALUES (100.00, 'Achat de fournitures', NOW(), 1);
