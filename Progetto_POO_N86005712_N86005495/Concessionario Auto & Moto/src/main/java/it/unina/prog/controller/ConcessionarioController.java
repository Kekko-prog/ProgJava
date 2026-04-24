package it.unina.prog.controller;

import it.unina.prog.dao.*;
import it.unina.prog.dao.impl.*;
import it.unina.prog.db.DatabaseManager;
import it.unina.prog.exception.DatabaseException;
import it.unina.prog.model.*;

import java.sql.*;
import java.util.*;

/**
 * Controller centrale dell'applicazione concessionario.
 * Responsabile di controllare tutte le operazioni di business logic:
 * - Autenticazione di clienti e dipendenti
 * - operazioni CRUD su clienti, veicoli, vendite, test drive
 * - Queries per report e storico
 * Delega la persistenza ai DAO (Data Access Objects) nella loro implementazione concreta.
 */
public class ConcessionarioController {
    private final ClienteDAO clienteDAO;
    private final DipendenteDAO dipendenteDAO;
    private final VeicoloDAO veicoloDAO;
    private final VenditaDAO venditaDAO;
    private final TestDriveDAO testDriveDAO;

    /**
     * Costruttore di default: istanzia i DAO nel loro default implementativo (impl).
     * Usato dalla GUI per creare il controller senza dipendenze esplicite
     */
    public ConcessionarioController() {
        this(new ClienteDAOImpl(), new DipendenteDAOImpl(), new VeicoloDAOImpl(), new VenditaDAOImpl(), new TestDriveDAOImpl());
    }

    /**
     * Costruttore per iniezione di dipendenze (utile per test).
     * Permette di passare mock o implementazioni alternative dei DAO.
     */
    public ConcessionarioController(ClienteDAO clienteDAO,
                                    DipendenteDAO dipendenteDAO,
                                    VeicoloDAO veicoloDAO,
                                    VenditaDAO venditaDAO,
                                    TestDriveDAO testDriveDAO) {
        this.clienteDAO = clienteDAO;
        this.dipendenteDAO = dipendenteDAO;
        this.veicoloDAO = veicoloDAO;
        this.venditaDAO = venditaDAO;
        this.testDriveDAO = testDriveDAO;
    }

    // ==================== AUTENTICAZIONE ====================

    /**
     * Autentica un cliente usando email e password.
     * @param email email del cliente
     * @param password password del cliente
     * @return oggetto Cliente se credenziali valide, altrimenti null
     * @throws DatabaseException se errore di database
     */
    public Cliente autenticaCliente(String email, String password) throws DatabaseException {
        return clienteDAO.autenticaCliente(email, password);
    }

    /**
     * Autentica un dipendente usando ID e password.
     * @param id id del dipendente
     * @param password password del dipendente
     * @return oggetto Dipendente se credenziali valide, altrimenti null
     * @throws DatabaseException se errore di database
     */
    public Dipendente autenticaDipendente(int id, String password) throws DatabaseException {
        return dipendenteDAO.autenticaDipendente(id, password);
    }

    // ==================== CRUD CLIENTI ====================

    /**
     * Recupera la lista completa di tutti i clienti dal database.
     * @return lista di oggetti Cliente
     * @throws DatabaseException se errore di database
     */
    public List<Cliente> getClienti() throws DatabaseException {
        return clienteDAO.getClienti();
    }

    /**
     * Recupera un cliente specifico cercandolo per email.
     * @param email email del cliente
     * @return oggetto Cliente o null se non trovato
     * @throws DatabaseException se errore di database
     */
    public Cliente getClienteByEmail(String email) throws DatabaseException {
        return clienteDAO.getClienteByEmail(email);
    }

    /**
     * Inserisce un nuovo cliente nel database.
     * @param nome nome del cliente
     * @param tipo tipo di cliente (es. "Privato", "Azienda")
     * @param email email del cliente
     * @param telefono numero di telefono
     * @param password password del cliente (sarà salvata in modo sicuro nel DB)
     * @throws DatabaseException se errore di database
     */
    public void inserisciCliente(String nome, String tipo, String email, String telefono, String password) throws DatabaseException {
        clienteDAO.inserisciCliente(nome, tipo, email, telefono, password);
    }

    /**
     * Aggiorna i dati di un cliente (senza modificare la password).
     * @param id id del cliente
     * @param nome nuovo nome
     * @param tipo nuovo tipo di cliente
     * @param email nuova email
     * @param telefono nuovo telefono
     * @throws DatabaseException se errore di database
     */
    public void aggiornaCliente(int id, String nome, String tipo, String email, String telefono) throws DatabaseException {
        clienteDAO.aggiornaCliente(id, nome, tipo, email, telefono);
    }

    /**
     * Aggiorna i dati di un cliente inclusa la password.
     * @param id id del cliente
     * @param nome nuovo nome
     * @param tipo nuovo tipo di cliente
     * @param email nuova email
     * @param telefono nuovo telefono
     * @param password nuova password
     * @throws DatabaseException se errore di database
     */
    public void aggiornaClienteConPassword(int id, String nome, String tipo, String email, String telefono, String password) throws DatabaseException {
        clienteDAO.aggiornaClienteConPassword(id, nome, tipo, email, telefono, password);
    }

    /**
     * Elimina completamente un cliente dal database.
     * @param id id del cliente da eliminare
     * @throws DatabaseException se errore di database
     */
    public void eliminaCliente(int id) throws DatabaseException {
        clienteDAO.eliminaCliente(id);
    }

    // ==================== CRUD VEICOLI ====================

    /**
     * Recupera tutti i veicoli presenti nel database, inclusi quelli già venduti.
     * @return lista di tutti i Veicoli
     * @throws DatabaseException se errore di database
     */
    public List<Veicolo> getAllVeicoli() throws DatabaseException {
        return veicoloDAO.getAllVeicoli();
    }

    /**
     * Recupera solo i veicoli disponibili (non ancora venduti) dal database.
     * Usato principalmente dal pannello di vendita per mostrare il catalogo.
     * @return lista di Veicoli con stato "Disponibile"
     * @throws DatabaseException se errore di database
     */
    public List<Veicolo> getVeicoliDisponibili() throws DatabaseException {
        return veicoloDAO.getVeicoliDisponibili();
    }

    /**
     * Inserisce un nuovo veicolo nel database.
     * @param targa targa del veicolo (es. "AB123CD")
     * @param marca marca/produttore (es. "Fiat", "BMW")
     * @param modello modello del veicolo (es. "500", "X3")
     * @param tipo tipo di veicolo (es. "Auto", "Moto")
     * @param prezzo prezzo di catalogo
     * @throws DatabaseException se errore di database
     */
    public void inserisciVeicolo(String targa, String marca, String modello, String tipo, double prezzo) throws DatabaseException {
        veicoloDAO.inserisciVeicolo(targa, marca, modello, tipo, prezzo);
    }

    /**
     * Aggiorna i dati di un veicolo esistente (identificato dalla targa).
     * @param targa targa del veicolo da aggiornare
     * @param marca nuova marca
     * @param modello nuovo modello
     * @param tipo nuovo tipo
     * @param prezzo nuovo prezzo
     * @throws DatabaseException se errore di database
     */
    public void aggiornaVeicolo(String targa, String marca, String modello, String tipo, double prezzo) throws DatabaseException {
        veicoloDAO.aggiornaVeicolo(targa, marca, modello, tipo, prezzo);
    }

    /**
     * Elimina un veicolo dal database (solitamente fatto solo se il veicolo non é stato ancora venduto).
     * @param targa targa del veicolo da eliminare
     * @throws DatabaseException se errore di database
     */
    public void eliminaVeicolo(String targa) throws DatabaseException {
        veicoloDAO.eliminaVeicolo(targa);
    }

    // ==================== VENDITE ====================

    /**
     * Registra una vendita nel database.
     * Questa è l'operazione critica che lega cliente, veicolo e dipendente.
     * Il prezzo finale può differire dal prezzo di catalogo per negoziazioni.
     * @param targa targa del veicolo venduto
     * @param clienteId id del cliente che acquista
     * @param dipendenteId id del dipendente che effettua la vendita
     * @param prezzoFinale prezzo finale di vendita (dopo eventuali sconti)
     * @throws DatabaseException se errore di database
     */
    public void effettuaVendita(String targa, int clienteId, int dipendenteId, double prezzoFinale) throws DatabaseException {
        venditaDAO.effettuaVendita(targa, clienteId, dipendenteId, prezzoFinale);
    }

    // ==================== TEST DRIVE ====================

    /**
     * Registra una richiesta di test drive nel database.
     * Permette al cliente di provare un veicolo prima di comprarlo.
     * @param clienteId id del cliente che richiede il test drive
     * @param targa targa del veicolo da testare
     * @param data data e ora richiesta per il test drive (formato: "YYYY-MM-DD HH:MM")
     * @throws DatabaseException se errore di database
     */
    public void richiediTestDrive(int clienteId, String targa, String data) throws DatabaseException {
        testDriveDAO.richiediTestDrive(clienteId, targa, data);
    }

    // ==================== PROFILO CLIENTE ====================

    /**
     * Recupera il profilo completo di un cliente dal database.
     * Usato nel pannello "Profilo" lato cliente.
     * @param clienteId id del cliente
     * @return oggetto Cliente con tutti i dati, o null se non trovato
     * @throws DatabaseException se errore di database
     */
    public Cliente getProfiloCliente(int clienteId) throws DatabaseException {
        String sql = "SELECT id, nome, tipo, email, telefono FROM Cliente WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
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
            throw new DatabaseException("Errore durante lettura profilo cliente", ex);
        }
    }

    /**
     * Aggiorna email e telefono di un cliente dal suo profilo personale.
     * @param clienteId id del cliente
     * @param email nuova email
     * @param telefono nuovo telefono
     * @throws DatabaseException se errore di database
     */
    public void aggiornaContattiCliente(int clienteId, String email, String telefono) throws DatabaseException {
        String sql = "UPDATE Cliente SET email = ?, telefono = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, telefono);
            ps.setInt(3, clienteId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante aggiornamento profilo cliente", ex);
        }
    }

    // ==================== REPORT E STORICO ====================

    /**
     * Recupera lo storico completo delle vendite effettuate a un cliente.
     * Ritorna una lista di Object[] con: [id, data, veicolo(targa), prezzo_finale, dipendente(nome)]
     * @param clienteId id del cliente
     * @return lista di righe (Object[]) della tabella di storico
     * @throws DatabaseException se errore di database
     */
    public List<Object[]> getStoricoVenditeCliente(int clienteId) throws DatabaseException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT v.id, v.data, v.veicolo, v.prezzo_finale, d.nome AS dipendente " +
                "FROM Vendita v JOIN Dipendente d ON v.dipendente = d.id " +
                "WHERE v.cliente = ? ORDER BY v.data DESC";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                            rs.getInt("id"),
                            rs.getString("data"),
                            rs.getString("veicolo"),
                            rs.getDouble("prezzo_finale"),
                            rs.getString("dipendente")
                    });
                }
            }
            return rows;
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante lettura storico vendite cliente", ex);
        }
    }

    /**
     * Recupera lo storico dei test drive effettuati da un cliente.
     * Ritorna una lista di Object[] con: [id, data, veicolo(targa)]
     * @param clienteId id del cliente
     * @return lista di righe (Object[]) della tabella di storico test drive
     * @throws DatabaseException se errore di database
     */
    public List<Object[]> getStoricoTestDriveCliente(int clienteId) throws DatabaseException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT id, data, veicolo FROM TestDrive WHERE cliente = ? ORDER BY data DESC";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{
                            rs.getInt("id"),
                            rs.getString("data"),
                            rs.getString("veicolo")
                    });
                }
            }
            return rows;
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante lettura storico test drive cliente", ex);
        }
    }

    /**
     * Recupera tutti gli ordini di vendita (vendite completate) del concessionario.
     * Vista generale per i dipendenti per monitorare le vendite.
     * Ritorna Object[] con: [id, data, veicolo(targa), cliente(nome), dipendente(nome), prezzo_finale]
     * @return lista di righe (Object[]) di tutte le vendite ordinate per data descrescente
     * @throws DatabaseException se errore di database
     */
    public List<Object[]> getOrdiniVendite() throws DatabaseException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT v.id, v.data, v.veicolo, c.nome as cliente, d.nome as dipendente, v.prezzo_finale " +
                "FROM Vendita v " +
                "JOIN Cliente c ON v.cliente = c.id " +
                "JOIN Dipendente d ON v.dipendente = d.id " +
                "ORDER BY v.data DESC";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("data"),
                        rs.getString("veicolo"),
                        rs.getString("cliente"),
                        rs.getString("dipendente"),
                        rs.getDouble("prezzo_finale")
                });
            }
            return rows;
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante lettura ordini vendita", ex);
        }
    }

    /**
     * Recupera tutti gli ordini di test drive (richieste di test drive) del concessionario.
     * Vista generale per i dipendenti per monitorare le richieste.
     * Ritorna Object[] con: [id, data, cliente(nome), veicolo(targa)]
     * @return lista di righe (Object[]) di tutti i test drive richiesti ordinati per data descrescente
     * @throws DatabaseException se errore di database
     */
    public List<Object[]> getOrdiniTestDrive() throws DatabaseException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT t.id, t.data, c.nome as cliente, t.veicolo " +
                "FROM TestDrive t JOIN Cliente c ON t.cliente = c.id ORDER BY t.data DESC";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rows.add(new Object[]{
                        rs.getInt("id"),
                        rs.getString("data"),
                        rs.getString("cliente"),
                        rs.getString("veicolo")
                });
            }
            return rows;
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante lettura ordini test drive", ex);
        }
    }

    // ==================== UTILITY ELIMINA ====================

    /**
     * Elimina una vendita specifica dal database.
     * Attenzione: questa è un'operazione delicata che modifica la storia transazionale.
     * @param idVendita id della vendita da eliminare
     * @throws DatabaseException se errore di database
     */
    public void eliminaVendita(int idVendita) throws DatabaseException {
        String sql = "DELETE FROM Vendita WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVendita);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante eliminazione vendita", ex);
        }
    }

    /**
     * Elimina una richiesta di test drive dal database.
     * @param idTestDrive id del test drive da eliminare
     * @throws DatabaseException se errore di database
     */
    public void eliminaTestDrive(int idTestDrive) throws DatabaseException {
        String sql = "DELETE FROM TestDrive WHERE id = ?";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTestDrive);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante eliminazione test drive", ex);
        }
    }

    // ==================== UTILITY VERIFICA ====================

    /**
     * Verifica se una targa è già presente in database.
     * La ricerca ignora gli spazi e la differenza tra maiuscole/minuscole.
     * Utile per evitare duplicati prima di inserire un nuovo veicolo.
     * @param targaInput targa da cercare
     * @return true se la targa esiste, false altrimenti
     * @throws DatabaseException se errore di database
     */
    public boolean targaExistsIgnoringCaseAndSpaces(String targaInput) throws DatabaseException {
        String sql = "SELECT 1 FROM Veicolo WHERE UPPER(REPLACE(targa, ' ', '')) = UPPER(REPLACE(?, ' ', ''))";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, targaInput);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante verifica targa", ex);
        }
    }

    /**
     * Verifica se una marca è già registrata nel database.
     * La ricerca ignora la differenza tra maiuscole/minuscole.
     * @param nomeMarca nome della marca da cercare (es. "Fiat", "BMW")
     * @return true se la marca esiste, false altrimenti
     * @throws DatabaseException se errore di database
     */
    public boolean marcaExists(String nomeMarca) throws DatabaseException {
        String sql = "SELECT 1 FROM Marca WHERE LOWER(nome) = LOWER(?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomeMarca);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante verifica marca", ex);
        }
    }

    /**
     * Crea una nuova marca nel database.
     * Questo permette di aggiungere dinamicamente nuove case automobilistiche.
     * @param nomeMarca nome della marca (es. "Tesla")
     * @param nazione nazione di provenienza della marca (es. "USA")
     * @throws DatabaseException se errore di database
     */
    public void creaMarca(String nomeMarca, String nazione) throws DatabaseException {
        String sql = "INSERT INTO Marca (nome, nazione) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomeMarca);
            ps.setString(2, nazione);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseException("Errore durante creazione marca", ex);
        }
    }
}