-- Création de la table des banques
CREATE TABLE IF NOT EXISTS banks (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(20) NOT NULL UNIQUE,
    adresse VARCHAR(200),
    telephone VARCHAR(20),
    date_creation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Index pour optimiser les performances
CREATE INDEX IF NOT EXISTS idx_banks_code ON banks(code);
CREATE INDEX IF NOT EXISTS idx_banks_nom ON banks(nom);

-- Ajout de la clé étrangère bank_id dans la table users
ALTER TABLE users ADD COLUMN IF NOT EXISTS bank_id BIGINT;
ALTER TABLE users ADD CONSTRAINT IF NOT EXISTS fk_users_bank FOREIGN KEY (bank_id) REFERENCES banks(id);

-- Index pour la recherche par banque
CREATE INDEX IF NOT EXISTS idx_users_bank_id ON users(bank_id);

-- Insertion de banques de test
INSERT INTO banks (nom, code, adresse, telephone) VALUES
('Banque Nationale', 'BNK001', '123 Rue Principale, Paris', '0123456789'),
('Banque Internationale', 'BNK002', '456 Avenue des Champs, Lyon', '0234567891'),
('Banque Régionale', 'BNK003', '789 Boulevard Sud, Marseille', '0345678912')
ON CONFLICT (code) DO NOTHING;