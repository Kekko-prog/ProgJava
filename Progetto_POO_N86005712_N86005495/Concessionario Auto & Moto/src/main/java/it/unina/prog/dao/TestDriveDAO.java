package it.unina.prog.dao;

import it.unina.prog.exception.DatabaseException;

/**
 * DAO Interface per la gestione delle richieste di Test Drive.
 * Responsabile di registrare le richieste di clienti di provare un veicolo.
 * L'implementazione concreta è in TestDriveDAOImpl.
 * 
 * Responsabilità:
 * - Registrazione di richieste di test drive da parte dei clienti
 * - Coordinamento tra cliente, veicolo e data/ora richiesta
 */
public interface TestDriveDAO {
    /**
     * Registra una nuova richiesta di test drive nel database.
     * @param clienteId id del cliente che richiede il test drive
     * @param targa targa del veicolo da testare
     * @param data data e ora richiesta (formato: "YYYY-MM-DD HH:MM:SS" o simile)
     * @throws DatabaseException se la registrazione fallisce
     */
    void richiediTestDrive(int clienteId, String targa, String data) throws DatabaseException;
}
