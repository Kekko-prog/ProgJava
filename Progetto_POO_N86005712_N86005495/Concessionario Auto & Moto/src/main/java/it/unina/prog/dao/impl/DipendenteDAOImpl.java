package it.unina.prog.dao.impl;

import it.unina.prog.dao.DipendenteDAO;
import it.unina.prog.db.DatabaseManager;
import it.unina.prog.exception.DatabaseException;
import it.unina.prog.model.Dipendente;

import java.sql.*;

/**
 * Implementazione concreta del DAO per Dipendenti.
 * Più semplice di ClienteDAOImpl poiché i dipendenti non possono auto-registrarsi.
 * Fornisce lettura e autenticazione dei dipendenti dal database.
 */
public class DipendenteDAOImpl implements DipendenteDAO {
    /**
     * Recupera i dati di un dipendente per ID.
     * Incluso la password per l'autenticazione.
     */
    @Override
    public Dipendente getDipendenteById(int id) throws DatabaseException {
        String sql = "SELECT id, nome, ruolo, password FROM Dipendente WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Dipendente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("ruolo"),
                            rs.getString("password")
                    );
                }
                return null;
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante lettura dipendente", ex);
        }
    }

    /**
     * Autentica un dipendente verificando ID e password.
     * Delega la lettura dati a getDipendenteById, poi verifica la password.
     */
    @Override
    public Dipendente autenticaDipendente(int id, String password) throws DatabaseException {
        Dipendente dipendente = getDipendenteById(id);
        if (dipendente == null) {
            return null;
        }
        return dipendente.getPassword().equals(password) ? dipendente : null;
    }
}