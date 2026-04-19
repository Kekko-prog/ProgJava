
-- INSERIMENTO MARCHE
INSERT INTO Marca (nome, nazione) VALUES
('Toyota', 'Giappone'),
('BMW', 'Germania');

-- INSERIMENTO CLIENTI
INSERT INTO Cliente (nome, tipo, email, telefono, password) VALUES
('Mario Rossi', 'privato', 'mario@example.com', '1234567890', 'mario123'),
('ACME Spa', 'azienda', 'acme@example.com', '0987654321', 'acme123');

-- INSERIMENTO DIPENDENTI
INSERT INTO Dipendente (nome, ruolo, password) VALUES
('Laura Bianchi', 'venditore', 'laura123'),
('Giovanni Verdi', 'amministratore', 'giovanni123');

-- INSERIMENTO VEICOLI
INSERT INTO Veicolo (targa, marca, modello, tipo, prezzo) VALUES
('AB123CD', 'Toyota', 'Yaris', 'auto', 15000.00),
('EF456GH', 'BMW', 'R1200', 'moto', 12000.00),
('ZX987YT', 'Toyota', 'Corolla', 'auto', 18000.00);

-- TEST DRIVE validi (Mario Rossi ne fa due)
INSERT INTO TestDrive (data, cliente, veicolo) VALUES
(CURRENT_DATE, 1, 'AB123CD'),
(CURRENT_DATE, 1, 'EF456GH');

-- TEST DRIVE che porta al limite (3 test drive per Mario Rossi)
INSERT INTO TestDrive (data, cliente, veicolo) VALUES
(CURRENT_DATE, 1, 'ZX987YT');

-- TEST DRIVE extra (commentato perché violerebbe il limite)
-- INSERT INTO TestDrive (data, cliente, veicolo) VALUES
-- (CURRENT_DATE, 1, 'EF456GH');

-- VENDITA di un veicolo a Mario Rossi (AB123CD)
INSERT INTO Vendita (data, veicolo, cliente, dipendente, prezzo_finale)
VALUES (CURRENT_DATE, 'AB123CD', 1, 1, 15000.00 * 1.10);

-- VENDITA di un altro veicolo ad ACME Spa (EF456GH)
INSERT INTO Vendita (data, veicolo, cliente, dipendente, prezzo_finale)
VALUES (CURRENT_DATE, 'EF456GH', 2, 2, 12000.00 * 1.22);

-- VENDITA duplicata (commentata perché il trigger la bloccherebbe)
-- INSERT INTO Vendita (data, veicolo, cliente, dipendente, prezzo_finale)
-- VALUES (CURRENT_DATE, 'AB123CD', 1, 1, 15000.00);

-- ESEMPIO DI USO DELLA FUNZIONE get_stato_veicolo
-- SELECT targa, get_stato_veicolo(targa) FROM Veicolo;
