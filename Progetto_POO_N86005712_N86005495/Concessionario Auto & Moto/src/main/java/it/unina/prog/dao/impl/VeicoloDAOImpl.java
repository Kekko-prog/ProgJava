package it.unina.prog.dao.impl;

import it.unina.prog.dao.VeicoloDAO;
import it.unina.prog.db.DatabaseManager;
import it.unina.prog.exception.DatabaseException;
import it.unina.prog.model.Veicolo;

import java.sql.*;
import java.util.*;

/**
 * Implementazione concreta del DAO per Veicoli.
 * Responsabile di gestire il catalogo dei veicoli:
 * - Inserimento e aggiornamento veicoli
 * - Queries per trovare veicoli disponibili
 * - Lettura dello stato (disponibile, venduto, manutenzione, etc.) via funzione stored
 */
public class VeicoloDAOImpl implements VeicoloDAO {
    /**
     * Inserisce un nuovo veicolo nel catalogo con stato iniziale "Disponibile".
     * La targa è unique key nel database.
     */
    @Override
    public void inserisciVeicolo(String targa, String marca, String modello, String tipo, double prezzo) throws DatabaseException {
        String sql = "INSERT INTO Veicolo (targa, marca, modello, tipo, prezzo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targa);
            ps.setString(2, marca);
            ps.setString(3, modello);
            ps.setString(4, tipo);
            ps.setDouble(5, prezzo);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante inserimento veicolo", ex);
        }
    }

    /**
     * Recupera lo stato attuale di un veicolo.
     * Lo stato è calcolato da una funzione stored nel database (get_stato_veicolo).
     * Possibili stati: "disponibile", "venduto", "in_manutenzione", etc.
     */
    @Override
    public String getStatoVeicolo(String targa) throws DatabaseException {
        String sql = "SELECT get_stato_veicolo(?) AS stato";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("stato");
                }
            }
            return "sconosciuto";
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante lettura stato veicolo", ex);
        }
    }

    /**
     * Recupera solo i veicoli con stato "disponibile".
     * Questo è il catalogo visibile ai clienti per la vendita.
     * Nota: fa una query per ogni veicolo per verificare lo stato (N+1 problem).
     * Per performance, considerare una single query con join a una tabella di stato.
     */
    @Override
    public List<Veicolo> getVeicoliDisponibili() throws DatabaseException {
        List<Veicolo> veicoli = new ArrayList<>();
        String sql = "SELECT * FROM Veicolo";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
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
            return veicoli;
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante lettura veicoli disponibili", ex);
        }
    }

    /**
     * Aggiorna i dettagli di un veicolo esistente (marca, modello, tipo, prezzo).
     * Non modifica lo stato (venduto/disponibile) qui.
     */
    @Override
    public void aggiornaVeicolo(String targa, String marca, String modello, String tipo, double prezzo) throws DatabaseException {
        String sql = "UPDATE Veicolo SET marca=?, modello=?, tipo=?, prezzo=? WHERE targa=?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, marca);
            ps.setString(2, modello);
            ps.setString(3, tipo);
            ps.setDouble(4, prezzo);
            ps.setString(5, targa);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante aggiornamento veicolo", ex);
        }
    }

    /**
     * Elimina un veicolo dal catalogo.
     * Operazione rara: di solito si elimina solo veicoli che non sono mai stati registrati in una vendita.
     */
    @Override
    public void eliminaVeicolo(String targa) throws DatabaseException {
        String sql = "DELETE FROM Veicolo WHERE targa=?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targa);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante eliminazione veicolo", ex);
        }
    }

    @Override
    public List<Veicolo> getAllVeicoli() throws DatabaseException {
        List<Veicolo> veicoli = new ArrayList<>();
        String sql = "SELECT * FROM Veicolo";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
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
            return veicoli;
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante lettura catalogo veicoli", ex);
        }
    }
}