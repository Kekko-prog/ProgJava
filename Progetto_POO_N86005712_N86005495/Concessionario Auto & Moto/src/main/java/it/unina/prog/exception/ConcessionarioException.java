package it.unina.prog.exception;

/**
 * Eccezione base per tutto il dominio dell'applicazione concessionario.
 * Estende Exception e serve da padre per eccezioni specifiche:
 * - DatabaseException (errori di persistenza)
 * - ValidationException (errori di validazione input)
 * 
 * Permette di catturare uniformemente tutti gli errori dell'app.
 */
public class ConcessionarioException extends Exception {
    /**
     * Costruttore con messaggio di errore.
     * @param message descrizione dell'errore
     */
    public ConcessionarioException(String message) {
        super(message);
    }

    /**
     * Costruttore con messaggio e causa.
     * Utile per wrappare eccezioni di librerie (es. SQLException da JDBC).
     * @param message descrizione dell'errore
     * @param cause eccezione sottostante che ha causato questo errore
     */
    public ConcessionarioException(String message, Throwable cause) {
        super(message, cause);
    }
}