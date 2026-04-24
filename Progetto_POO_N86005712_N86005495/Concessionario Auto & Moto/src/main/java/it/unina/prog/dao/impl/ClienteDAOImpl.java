package it.unina.prog.dao.impl;

import it.unina.prog.dao.ClienteDAO;
import it.unina.prog.db.DatabaseManager;
import it.unina.prog.exception.DatabaseException;
import it.unina.prog.model.Cliente;

import java.sql.*;
import java.util.*;

/**
 * Implementazione concreta del DAO per Clienti.
 * Responsabile di tutte le operazioni CRUD direttamente su database:
 * - Inserimento, lettura, aggiornamento, eliminazione di clienti
 * - Autenticazione via email/password
 * - Uso di try-with-resources per garantire chiusura corretta connessioni/statement
 * - Wrapping di SQLException in DatabaseException
 */
public class ClienteDAOImpl implements ClienteDAO {
    /**
     * Inserisce un nuovo cliente nel database.
     * Usa PreparedStatement per prevenire SQL injection.
     */
    @Override
    public void inserisciCliente(String nome, String tipo, String email, String telefono, String password) throws DatabaseException {
        String sql = "INSERT INTO Cliente (nome, tipo, email, telefono, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, tipo);
            ps.setString(3, email);
            ps.setString(4, telefono);
            ps.setString(5, password);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante inserimento cliente", ex);
        }
    }

    /**
     * Recupera tutti i clienti dal database, creando una lista di oggetti Cliente.
     * Attenzione: per dataset molto grandi, considerare paginazione.
     */
    @Override
    public List<Cliente> getClienti() throws DatabaseException {
        List<Cliente> clienti = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
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
            return clienti;
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante lettura clienti", ex);
        }
    }

    /**
     * Ricerca un cliente per email (case-insensitive).
     * Ritorna null se nessun cliente trovato.
     */
    @Override
    public Cliente getClienteByEmail(String email) throws DatabaseException {
        String sql = "SELECT id, nome, tipo, email, telefono FROM Cliente WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
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
                return null;
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante ricerca cliente per email", ex);
        }
    }

    /**
     * Autentica un cliente verificando email e password.
     * Nota: questo confronto di password è in chiaro (non hashato).
     * In produzione, usare algoritmi di hashing come bcrypt.
     * Ritorna null se credenziali non valide.
     */
    @Override
    public Cliente autenticaCliente(String email, String password) throws DatabaseException {
        String sql = "SELECT id, nome, tipo, email, telefono, password FROM Cliente WHERE LOWER(email) = LOWER(?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
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
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante autenticazione cliente", ex);
        }
    }

    /**
     * Aggiorna i dati di un cliente senza modificare la password.
     * Usa parametri PreparedStatement per tipo-safety.
     */
    @Override
    public void aggiornaCliente(int id, String nome, String tipo, String email, String telefono) throws DatabaseException {
        String sql = "UPDATE Cliente SET nome=?, tipo=?, email=?, telefono=? WHERE id=?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, tipo);
            ps.setString(3, email);
            ps.setString(4, telefono);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante aggiornamento cliente", ex);
        }
    }

    /**
     * Aggiorna i dati di un cliente inclusa la password.
     * Operazione sensibile: dovrebbe essere ristretta a utenti admin o al cliente stesso.
     */
    @Override
    public void aggiornaClienteConPassword(int id, String nome, String tipo, String email, String telefono, String password) throws DatabaseException {
        String sql = "UPDATE Cliente SET nome=?, tipo=?, email=?, telefono=?, password=? WHERE id=?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setString(2, tipo);
            ps.setString(3, email);
            ps.setString(4, telefono);
            ps.setString(5, password);
            ps.setInt(6, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante aggiornamento password cliente", ex);
        }
    }

    /**
     * Elimina un cliente dal database.
     * Operazione critica: il database avrà cascading delete per mantenere integrità referenziale.
     */
    @Override
    public void eliminaCliente(int id) throws DatabaseException {
        String sql = "DELETE FROM Cliente WHERE id=?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante eliminazione cliente", ex);
        }
    }
}