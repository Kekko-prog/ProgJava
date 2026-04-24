package it.unina.prog.dao.impl;

import it.unina.prog.dao.VenditaDAO;
import it.unina.prog.db.DatabaseManager;
import it.unina.prog.exception.DatabaseException;

import java.sql.*;

public class VenditaDAOImpl implements VenditaDAO {
    /**
     * Registra una vendita nel database.
     * Operazione transazionale: crea record nella tabella Vendita con data corrente (CURRENT_DATE).
     * Vincoli DB assicurano che cliente, dipendente e veicolo esistono.
     */
    @Override
    public void effettuaVendita(String targa, int clienteId, int dipendenteId, double prezzoFinale) throws DatabaseException {
        String sql = "INSERT INTO Vendita (data, veicolo, cliente, dipendente, prezzo_finale) VALUES (CURRENT_DATE, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targa);
            ps.setInt(2, clienteId);
            ps.setInt(3, dipendenteId);
            ps.setDouble(4, prezzoFinale);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante registrazione vendita", ex);
        }
    }
}