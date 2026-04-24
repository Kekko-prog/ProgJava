package it.unina.prog.db;

import it.unina.prog.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton che gestisce la connessione al database PostgreSQL.
 * Responsabile di:
 * - Mantenere parametri di connessione centralizzati
 * - Fornire connessioni thread-safe al resto dell'applicazione
 * - Gestire errori di connessione via DatabaseException
 * 
 * Utilizzato da tutti i DAO per ottenere una connessione valida.
 */
public final class DatabaseManager {
    // Parametri hardcoded di connessione al database PostgreSQL locale.
    // TODO: considerare di spostarli in un file di configurazione properties.
    private static final String URL = "jdbc:postgresql://localhost:5432/concessionario_db";
    private static final String USER = "concessionario_admin";
    private static final String PASSWORD = "password123";

    // Istanza singleton (lazy initialization con synchronization).
    private static DatabaseManager instance;

    /**
     * Costruttore privato: previene creazioni multiple istanze.
     * Il pattern Singleton garantisce una sola connessione factory per tutta l'app.
     */
    private DatabaseManager() {
    }

    /**
     * Ottiene l'istanza singleton di DatabaseManager.
     * Implementazione thread-safe con synchronized per lazy initialization.
     * @return l'istanza unica di DatabaseManager
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Crea e ritorna una nuova connessione al database.
     * Nota: ognuna è una connessione fresca, NON pooled.
     * Per migliore performance in produzione, valutare l'uso di HikariCP o simile.
     * @return Connection valida al database PostgreSQL
     * @throws DatabaseException se la connessione non riesce (server down, credenziali sbagliate, etc.)
     */
    public Connection getConnection() throws DatabaseException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            throw new DatabaseException("Impossibile connettersi al database", ex);
        }
    }
}