package it.unina.prog.dao;

import it.unina.prog.exception.DatabaseException;
import it.unina.prog.model.Cliente;

import java.util.*;

/**
 * DAO Interface per la gestione dei Clienti.
 * Definisce i contratti per tutte le operazioni di CRUD sui clienti.
 * L'implementazione concreta è in ClienteDAOImpl.
 * 
 * Responsabilità:
 * - Autenticazione dei clienti (login)
 * - CRUD completo sui dati del cliente
 * - Queries per recuperare clienti da vari criteri
 */
public interface ClienteDAO {
    /**
     * Inserisce un nuovo cliente nel database.
     * @param nome nome del cliente
     * @param tipo tipo (es. "Privato")
     * @param email email di contatto
     * @param telefono numero di telefono
     * @param password password per il login
     * @throws DatabaseException se l'inserimento fallisce
     */
    void inserisciCliente(String nome, String tipo, String email, String telefono, String password) throws DatabaseException;

    /**
     * Recupera la lista completa di tutti i clienti.
     * @return lista di oggetti Cliente
     * @throws DatabaseException se la query fallisce
     */
    List<Cliente> getClienti() throws DatabaseException;

    /**
     * Recupera un cliente specifico per email.
     * @param email email del cliente da cercare
     * @return oggetto Cliente o null se non trovato
     * @throws DatabaseException se la query fallisce
     */
    Cliente getClienteByEmail(String email) throws DatabaseException;

    /**
     * Autentica un cliente verificando email e password.
     * @param email email del cliente
     * @param password password inserita dall'utente
     * @return oggetto Cliente se credenziali valide, null altrimenti
     * @throws DatabaseException se la verifica fallisce
     */
    Cliente autenticaCliente(String email, String password) throws DatabaseException;

    /**
     * Aggiorna i dati di un cliente (senza modif password).
     * @param id id del cliente da aggiornare
     * @param nome nuovo nome
     * @param tipo nuovo tipo
     * @param email nuova email
     * @param telefono nuovo telefono
     * @throws DatabaseException se l'aggiornamento fallisce
     */
    void aggiornaCliente(int id, String nome, String tipo, String email, String telefono) throws DatabaseException;

    /**
     * Aggiorna i dati di un cliente inclusa la password.
     * @param id id del cliente da aggiornare
     * @param nome nuovo nome
     * @param tipo nuovo tipo
     * @param email nuova email
     * @param telefono nuovo telefono
     * @param password nuova password
     * @throws DatabaseException se l'aggiornamento fallisce
     */
    void aggiornaClienteConPassword(int id, String nome, String tipo, String email, String telefono, String password) throws DatabaseException;

    /**
     * Elimina completamente un cliente dal database.
     * @param id id del cliente da eliminare
     * @throws DatabaseException se l'eliminazione fallisce
     */
    void eliminaCliente(int id) throws DatabaseException;
}
