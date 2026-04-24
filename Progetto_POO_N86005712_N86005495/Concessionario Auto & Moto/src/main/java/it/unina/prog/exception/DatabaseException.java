package it.unina.prog.exception;

/**
 * Eccezione specifica per errori di database.
 * Estende ConcessionarioException e viene sollevata quando:
 * - Fallisce una connessione al DB
 * - Una query SQL fallisce
 * - Si verifica un'inconsistenza di dati
 * 
 * Sempre wrappata attorno a SQLException per nascondere i dettagli JDBC dalla UI.
 */
public class DatabaseException extends ConcessionarioException {
    /**
     * Costruttore con messaggio e causa (solitamente una SQLException).
     * @param message descrizione dell'operazione che è fallita
     * @param cause SQLException sottostante
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}