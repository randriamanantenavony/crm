-- Création de la base de données
CREATE DATABASE IF NOT EXISTS budget_management;
USE budget_management;

-- Table Customer (Clients)
CREATE TABLE IF NOT EXISTS Customer (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Table Budget (Budgets)
CREATE TABLE IF NOT EXISTS Budget (
    budget_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    alert_threshold DECIMAL(5, 2) NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id) ON DELETE CASCADE
);

-- Table Expense (Dépenses)
CREATE TABLE IF NOT EXISTS Expense (
    expense_id INT AUTO_INCREMENT PRIMARY KEY,
    budget_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    type VARCHAR(50) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (budget_id) REFERENCES Budget(budget_id) ON DELETE CASCADE
);

-- Table Alert (Alertes)
CREATE TABLE IF NOT EXISTS Alert (
    alert_id INT AUTO_INCREMENT PRIMARY KEY,
    budget_id INT NOT NULL,
    message VARCHAR(255) NOT NULL,
    triggered_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (budget_id) REFERENCES Budget(budget_id) ON DELETE CASCADE
);

-- Table Confirmation (Confirmations)
CREATE TABLE IF NOT EXISTS Confirmation (
    confirmation_id INT AUTO_INCREMENT PRIMARY KEY,
    expense_id INT NOT NULL,
    message VARCHAR(255) NOT NULL,
    status VARCHAR(50) DEFAULT 'En attente',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (expense_id) REFERENCES Expense(expense_id) ON DELETE CASCADE
);

-- Insertion de données de test

-- Insertion d'un client
INSERT INTO Customer (name, email) VALUES ('Société XYZ', 'contact@xyz.com');

-- Insertion d'un budget pour le client
INSERT INTO Budget (customer_id, name, total_amount, alert_threshold, start_date, end_date)
VALUES (1, 'Marketing', 10000.00, 80.00, '2023-10-01 00:00:00', '2023-12-31 23:59:59');

-- Insertion de dépenses
INSERT INTO Expense (budget_id, description, amount, type)
VALUES (1, 'Campagne publicitaire', 1500.00, 'Lead'),
       (1, 'Résolution ticket support', 200.00, 'Ticket');

-- Insertion d'une alerte
INSERT INTO Alert (budget_id, message)
VALUES (1, 'Alerte : 80 % du budget Marketing dépensé.');

-- Insertion d'une confirmation
INSERT INTO Confirmation (expense_id, message)
VALUES (2, 'Dépassement de budget. Confirmez-vous ?');

-- Requêtes de test

-- Récupérer tous les budgets d'un client
SELECT * FROM Budget WHERE customer_id = 1;

-- Calculer le total des dépenses pour un budget
SELECT budget_id, SUM(amount) AS total_expenses
FROM Expense
WHERE budget_id = 1;

-- Vérifier si une alerte doit être déclenchée
SELECT b.budget_id, b.total_amount, b.alert_threshold, SUM(e.amount) AS total_expenses
FROM Budget b
LEFT JOIN Expense e ON b.budget_id = e.budget_id
WHERE b.budget_id = 1
GROUP BY b.budget_id
HAVING (SUM(e.amount) / b.total_amount * 100 >= b.alert_threshold;

-- Vérifier si un dépassement de budget a lieu
SELECT b.budget_id, b.total_amount, SUM(e.amount) AS total_expenses
FROM Budget b
LEFT JOIN Expense e ON b.budget_id = e.budget_id
WHERE b.budget_id = 1
GROUP BY b.budget_id
HAVING SUM(e.amount) > b.total_amount;