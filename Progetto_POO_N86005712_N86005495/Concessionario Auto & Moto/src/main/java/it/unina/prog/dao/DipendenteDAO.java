package it.unina.prog.dao;

import it.unina.prog.exception.DatabaseException;
import it.unina.prog.model.Dipendente;

/**
 * DAO Interface per la gestione dei Dipendenti.
 * Meno ricca di ClienteDAO poiché i dipendenti non possono self-registrarsi.
 * L'implementazione concreta è in DipendenteDAOImpl.
 * 
 * Responsabilità:
 * - Autenticazione dei dipendenti (login)
 * - Lettura dei dati del dipendente
 */
public interface DipendenteDAO {
    /**
     * Recupera un dipendente per ID.
     * @param id id del dipendente
     * @return oggetto Dipendente o null se non trovato
     * @throws DatabaseException se la query fallisce
     */
    Dipendente getDipendenteById(int id) throws DatabaseException;

    /**
     * Autentica un dipendente verificando ID e password.
     * @param id id del dipendente
     * @param password password inserita dall'utente
     * @return oggetto Dipendente se credenziali valide, null altrimenti
     * @throws DatabaseException se la verifica fallisce
     */
    Dipendente autenticaDipendente(int id, String password) throws DatabaseException;
}
