package it.unina.prog.dao;

import it.unina.prog.DBManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestDriveDAO {
    public static void richiediTestDrive(int clienteId, String targa, String data) throws SQLException {
        String sql = "INSERT INTO TestDrive (data, cliente, veicolo) VALUES (?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(data));
            ps.setInt(2, clienteId);
            ps.setString(3, targa);
            ps.executeUpdate();
        }
    }
}
