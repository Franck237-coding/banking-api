-- Création de la table des utilisateurs
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telephone VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Création de la table des comptes
CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    numero_compte VARCHAR(50) NOT NULL UNIQUE,
    solde DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    type_compte VARCHAR(20) NOT NULL DEFAULT 'COURANT',
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Création de la table des transactions
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    reference VARCHAR(50) NOT NULL UNIQUE,
    montant DECIMAL(15,2) NOT NULL,
    type_transaction VARCHAR(20) NOT NULL,
    description VARCHAR(500),
    date_transaction TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(20) NOT NULL DEFAULT 'EN_ATTENTE',
    compte_source_id BIGINT,
    compte_destination_id BIGINT,
    FOREIGN KEY (compte_source_id) REFERENCES accounts(id),
    FOREIGN KEY (compte_destination_id) REFERENCES accounts(id)
);

-- Index pour optimiser les performances
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_accounts_numero ON accounts(numero_compte);
CREATE INDEX IF NOT EXISTS idx_accounts_user_id ON accounts(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_reference ON transactions(reference);
CREATE INDEX IF NOT EXISTS idx_transactions_compte_source ON transactions(compte_source_id);
CREATE INDEX IF NOT EXISTS idx_transactions_compte_dest ON transactions(compte_destination_id);
CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(date_transaction);

-- Insertion de données de test
INSERT INTO users (nom, prenom, email, telephone, role) VALUES
('Dupont', 'Jean', 'jean.dupont@email.com', '0123456789', 'USER'),
('Martin', 'Marie', 'marie.martin@email.com', '0234567891', 'USER'),
('Admin', 'System', 'admin@banking.com', '0345678912', 'ADMIN')
ON CONFLICT (email) DO NOTHING;

-- Insertion de comptes de test
INSERT INTO accounts (numero_compte, solde, type_compte, user_id) VALUES
('BANK123456789012', 1000.00, 'COURANT', 1),
('BANK234567890123', 2500.50, 'EPARGNE', 1),
('BANK345678901234', 500.25, 'COURANT', 2)
ON CONFLICT (numero_compte) DO NOTHING;
