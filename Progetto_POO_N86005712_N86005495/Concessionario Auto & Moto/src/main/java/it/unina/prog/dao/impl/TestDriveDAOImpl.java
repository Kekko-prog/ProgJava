package it.unina.prog.dao.impl;

import it.unina.prog.dao.TestDriveDAO;
import it.unina.prog.db.DatabaseManager;
import it.unina.prog.exception.DatabaseException;

import java.sql.*;

/**
 * Implementazione concreta del DAO per Test Drive.
 * Gestisce le richieste di clienti di provare un veicolo prima dell'acquisto.
 */
public class TestDriveDAOImpl implements TestDriveDAO {
    /**
     * Registra una nuova richiesta di test drive.
     * Vincoli DB verificano che il cliente non abbia superato il limite di 3 test drive al mese.
     */
    @Override
    public void richiediTestDrive(int clienteId, String targa, String data) throws DatabaseException {
        String sql = "INSERT INTO TestDrive (data, cliente, veicolo) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(data));
            ps.setInt(2, clienteId);
            ps.setString(3, targa);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante registrazione test drive", ex);
        }
    }
}