
-- RUOLO E DATABASE (opzionale se già esistente)
CREATE ROLE concessionario_admin WITH LOGIN PASSWORD 'password123';
ALTER ROLE concessionario_admin CREATEDB;
CREATE DATABASE concessionario_db OWNER concessionario_admin;

-- TABELLE
CREATE TABLE Marca (
    nome TEXT PRIMARY KEY,
    nazione TEXT NOT NULL
);

CREATE TABLE Cliente (
    id SERIAL PRIMARY KEY,
    nome TEXT NOT NULL,
    tipo TEXT CHECK (tipo IN ('privato', 'azienda')) NOT NULL,
    email TEXT UNIQUE NOT NULL,
    telefono TEXT,
    password TEXT NOT NULL
);

CREATE TABLE Dipendente (
    id SERIAL PRIMARY KEY,
    nome TEXT NOT NULL,
    ruolo TEXT NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE Veicolo (
    targa TEXT PRIMARY KEY,
    marca TEXT REFERENCES Marca(nome),
    modello TEXT NOT NULL,
    tipo TEXT CHECK (tipo IN ('auto', 'moto')) NOT NULL,
    prezzo NUMERIC(10,2) NOT NULL
);

CREATE TABLE Vendita (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    veicolo TEXT,
    cliente INT,
    dipendente INT,
    prezzo_finale NUMERIC(10,2) NOT NULL,
    CONSTRAINT vendita_veicolo_fkey FOREIGN KEY (veicolo) REFERENCES Veicolo(targa) ON DELETE CASCADE,
    CONSTRAINT vendita_cliente_fkey FOREIGN KEY (cliente) REFERENCES Cliente(id) ON DELETE CASCADE,
    CONSTRAINT vendita_dipendente_fkey FOREIGN KEY (dipendente) REFERENCES Dipendente(id)
);

CREATE TABLE TestDrive (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    cliente INT,
    veicolo TEXT,
    CONSTRAINT testdrive_cliente_fkey FOREIGN KEY (cliente) REFERENCES Cliente(id) ON DELETE CASCADE,
    CONSTRAINT testdrive_veicolo_fkey FOREIGN KEY (veicolo) REFERENCES Veicolo(targa) ON DELETE CASCADE
);

-- FUNZIONE: verifica che il veicolo non sia già stato venduto
CREATE OR REPLACE FUNCTION check_veicolo_disponibile()
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (SELECT 1 FROM Vendita WHERE veicolo = NEW.veicolo) THEN
        RAISE EXCEPTION 'Il veicolo è già stato venduto';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- TRIGGER su Vendita
CREATE TRIGGER trg_check_veicolo_venduto
BEFORE INSERT ON Vendita
FOR EACH ROW EXECUTE FUNCTION check_veicolo_disponibile();

-- FUNZIONE: massimo 3 test drive al mese per cliente
CREATE OR REPLACE FUNCTION check_testdrive_limite()
RETURNS TRIGGER AS $$
DECLARE
    count_td INT;
BEGIN
    SELECT COUNT(*) INTO count_td FROM TestDrive
    WHERE cliente = NEW.cliente
    AND date_part('month', data) = date_part('month', NEW.data)
    AND date_part('year', data) = date_part('year', NEW.data);

    IF count_td >= 3 THEN
        RAISE EXCEPTION 'Limite massimo di 3 test drive al mese raggiunto';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- TRIGGER su TestDrive
CREATE TRIGGER trg_check_testdrive
BEFORE INSERT ON TestDrive
FOR EACH ROW EXECUTE FUNCTION check_testdrive_limite();

-- FUNZIONE: restituisce lo stato di un veicolo
CREATE OR REPLACE FUNCTION get_stato_veicolo(targa TEXT)
RETURNS TEXT AS $$
DECLARE
    stato TEXT;
BEGIN
    IF EXISTS (SELECT 1 FROM Vendita WHERE veicolo = targa) THEN
        stato := 'venduto';
    ELSIF EXISTS (SELECT 1 FROM TestDrive WHERE veicolo = targa AND data = CURRENT_DATE) THEN
        stato := 'non disponibile momentaneamente';
    ELSE
        stato := 'disponibile';
    END IF;
    RETURN stato;
END;
$$ LANGUAGE plpgsql;



GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO concessionario_admin;

-- Dai anche i permessi su sequenze (per usare SERIAL/id)
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO concessionario_admin;