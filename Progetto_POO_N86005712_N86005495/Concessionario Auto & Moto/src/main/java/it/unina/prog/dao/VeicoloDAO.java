package it.unina.prog.dao;

import it.unina.prog.DBManager;
import it.unina.prog.model.Veicolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VeicoloDAO {
    public static void inserisciVeicolo(String targa, String marca, String modello, String tipo, double prezzo) throws SQLException {
        String sql = "INSERT INTO Veicolo (targa, marca, modello, tipo, prezzo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targa);
            ps.setString(2, marca);
            ps.setString(3, modello);
            ps.setString(4, tipo);
            ps.setDouble(5, prezzo);
            ps.executeUpdate();
        }
    }

    public static String getStatoVeicolo(String targa) throws SQLException {
        String sql = "SELECT get_stato_veicolo(?) AS stato";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("stato");
                }
            }
        }
        return "sconosciuto";
    }

    public static List<Veicolo> getVeicoliDisponibili() throws SQLException {
        List<Veicolo> veicoli = new ArrayList<>();
        String sql = "SELECT * FROM Veicolo";
        try (Connection conn = DBManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String targa = rs.getString("targa");
                String stato = getStatoVeicolo(targa);
                if ("disponibile".equals(stato)) {
                    veicoli.add(new Veicolo(
                        targa,
                        rs.getString("marca"),
                        rs.getString("modello"),
                        rs.getString("tipo"),
                        rs.getDouble("prezzo"),
                        stato
                    ));
                }
            }
        }
        return veicoli;
    }

    public static void aggiornaVeicolo(String targa, String marca, String modello, String tipo, double prezzo) throws SQLException {
        String sql = "UPDATE Veicolo SET marca=?, modello=?, tipo=?, prezzo=? WHERE targa=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, marca);
            ps.setString(2, modello);
            ps.setString(3, tipo);
            ps.setDouble(4, prezzo);
            ps.setString(5, targa);
            ps.executeUpdate();
        }
    }

    public static void eliminaVeicolo(String targa) throws SQLException {
        String sql = "DELETE FROM Veicolo WHERE targa=?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targa);
            ps.executeUpdate();
        }
    }

    public static List<Veicolo> getAllVeicoli() throws SQLException {
        List<Veicolo> veicoli = new ArrayList<>();
        String sql = "SELECT * FROM Veicolo";
        try (Connection conn = DBManager.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String targa = rs.getString("targa");
                String stato = getStatoVeicolo(targa);
                veicoli.add(new Veicolo(
                    targa,
                    rs.getString("marca"),
                    rs.getString("modello"),
                    rs.getString("tipo"),
                    rs.getDouble("prezzo"),
                    stato
                ));
            }
        }
        return veicoli;
    }
}
