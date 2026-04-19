package it.unina.prog.dao;

import it.unina.prog.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VenditaDAO {
    public static void effettuaVendita(String targa, int clienteId, int dipendenteId, double prezzoFinale) throws SQLException {
        String sql = "INSERT INTO Vendita (data, veicolo, cliente, dipendente, prezzo_finale) VALUES (CURRENT_DATE, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targa);
            ps.setInt(2, clienteId);
            ps.setInt(3, dipendenteId);
            ps.setDouble(4, prezzoFinale);
            ps.executeUpdate();
        }
    }
}
