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

CREATE TABLE IF NOT EXISTS Expense (
    expense_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    type VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE
);

-- Table Expense (Dépenses globales)
CREATE TABLE IF NOT EXISTS Expense (
    expense_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    type VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE
);

-- Table Alert (Alertes)
CREATE TABLE IF NOT EXISTS Alert (
    alert_id INT AUTO_INCREMENT PRIMARY KEY,
    budget_id INT NOT NULL,
    message VARCHAR(255) NOT NULL,
    triggered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (budget_id) REFERENCES Budget(budget_id) ON DELETE CASCADE
);

-- Insertion de données de test

-- Insertion d'un client
INSERT INTO Customer (name, email) VALUES ('Société XYZ', 'contact@xyz.com');

-- Insertion de budgets pour le client
INSERT INTO Budget (customer_id, name, total_amount, alert_threshold, start_date, end_date)
VALUES (1, 'Marketing', 10000.00, 80.00, '2023-10-01 00:00:00', '2023-12-31 23:59:59'),
       (1, 'Développement', 20000.00, 90.00, '2023-10-01 00:00:00', '2023-12-31 23:59:59');

-- Insertion de dépenses globales
INSERT INTO Expense (customer_id, description, amount, type)
VALUES (1, 'Campagne publicitaire', 1500.00, 'Lead'),
       (1, 'Résolution ticket support', 200.00, 'Ticket');

-- Insertion d'une alerte
INSERT INTO Alert (budget_id, message)
VALUES (1, 'Alerte : 80 % du budget Marketing dépensé.');

-- Requêtes de test

-- Récupérer tous les budgets d'un client
SELECT * FROM Budget WHERE customer_id = 1;

-- Récupérer toutes les dépenses globales d'un client
SELECT * FROM Expense WHERE customer_id = 1;

-- Calculer le total des dépenses globales pour un client
SELECT customer_id, SUM(amount) AS total_expenses
FROM Expense
WHERE customer_id = 1
GROUP BY customer_id;

-- Vérifier si une alerte doit être déclenchée pour un budget
SELECT b.budget_id, b.total_amount, b.alert_threshold, SUM(e.amount) AS total_expenses
FROM Budget b
LEFT JOIN Expense e ON b.customer_id = e.customer_id
WHERE b.budget_id = 1
GROUP BY b.budget_id
HAVING (SUM(e.amount) / b.total_amount * 100) >= b.alert_threshold;