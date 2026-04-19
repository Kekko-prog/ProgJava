package it.unina.prog.dao;

import it.unina.prog.DBManager;
import it.unina.prog.model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    public static void inserisciCliente(String nome, String tipo, String email, String telefono, String password) throws SQLException {
        String sql = "INSERT INTO Cliente (nome, tipo, email, telefono, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, tipo);
            ps.setString(3, email);
            ps.setString(4, telefono);
            ps.setString(5, password);
            ps.executeUpdate();
        }
    }

    public static List<Cliente> getClienti() throws SQLException {
        List<Cliente> clienti = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Connection conn = DBManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                clienti.add(new Cliente(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("tipo"),
                    rs.getString("email"),
                    rs.getString("telefono")
                ));
            }
        }
        return clienti;
    }

    public static Cliente getClienteByEmail(String email) throws SQLException {
        String sql = "SELECT id, nome, tipo, email, telefono FROM Cliente WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("tipo"),
                            rs.getString("email"),
                            rs.getString("telefono")
                    );
                }
            }
        }
        return null;
    }

    public static Cliente autenticaCliente(String email, String password) throws SQLException {
        String sql = "SELECT id, nome, tipo, email, telefono, password FROM Cliente WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String passwordDb = rs.getString("password");
                if (!password.equals(passwordDb)) {
                    return null;
                }
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("tipo"),
                        rs.getString("email"),
                        rs.getString("telefono")
                );
            }
        }
    }

    public static void aggiornaCliente(int id, String nome, String tipo, String email, String telefono) throws SQLException {
        String sql = "UPDATE Cliente SET nome=?, tipo=?, email=?, telefono=? WHERE id=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, tipo);
            ps.setString(3, email);
            ps.setString(4, telefono);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
    }

    public static void aggiornaClienteConPassword(int id, String nome, String tipo, String email, String telefono, String password) throws SQLException {
        String sql = "UPDATE Cliente SET nome=?, tipo=?, email=?, telefono=?, password=? WHERE id=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, tipo);
            ps.setString(3, email);
            ps.setString(4, telefono);
            ps.setString(5, password);
            ps.setInt(6, id);
            ps.executeUpdate();
        }
    }

    public static void eliminaCliente(int id) throws SQLException {
        String sql = "DELETE FROM Cliente WHERE id=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
