package it.unina.prog.dao;

import it.unina.prog.DBManager;
import it.unina.prog.model.Dipendente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class DipendenteDAO {
    private DipendenteDAO() {
    }

    public static Dipendente getDipendenteById(int id) throws SQLException {
        String sql = "SELECT id, nome, ruolo, password FROM Dipendente WHERE id = ?";
        try (Connection conn = DBManager.getConnection();
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
            }
        }
        return null;
    }

    public static Dipendente autenticaDipendente(int id, String password) throws SQLException {
        Dipendente dipendente = getDipendenteById(id);
        if (dipendente == null) {
            return null;
        }
        if (!dipendente.getPassword().equals(password)) {
            return null;
        }
        return dipendente;
    }
}
