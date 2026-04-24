package it.unina.prog;

import it.unina.prog.db.DatabaseManager;
import it.unina.prog.exception.DatabaseException;

import java.sql.*;

/**
 * Wrapper di compatibilità per l'accesso al database.
 * Mantiene un'interfaccia semplice compatibile con il codice legacy.
 * Delega al DatabaseManager singleton per ottenere le connessioni.
 * 
 * Questa classe potrebbe essere deprecata; preferire l'uso diretto di DatabaseManager
 * nei nuovi sviluppi per evitare layer di indirezione non necessari.
 */
public class DBManager {
    /**
     * Ottiene una connessione al database, incapsulando le eccezioni DatabaseException
     * in SQLException per compatibilità con vecchio codice.
     * @return Connection valida al database
     * @throws SQLException se la connessione fallisce
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DatabaseManager.getInstance().getConnection();
        } catch (DatabaseException ex) {
            throw new SQLException(ex.getMessage(), ex);
        }
    }
}
