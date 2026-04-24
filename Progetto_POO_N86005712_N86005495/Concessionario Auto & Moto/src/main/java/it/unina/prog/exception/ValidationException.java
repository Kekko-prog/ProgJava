package it.unina.prog.exception;

/**
 * Eccezione per errori di validazione dell'input.
 * Sollevata quando:
 * - Input utente non passa criteri di validazione
 * - Dati obbligatori sono mancanti
 * - Formato di campo non è valido (es. email malformata, numero negativo per prezzo)
 * 
 * Usata principalmente dalle viste GUI per mostrare messaggi di errore all'utente.
 */
public class ValidationException extends ConcessionarioException {
    /**
     * Costruttore con messaggio di errore di validazione.
     * @param message descrizione dettagliata del problema di validazione
     */
    public ValidationException(String message) {
        super(message);
    }
}