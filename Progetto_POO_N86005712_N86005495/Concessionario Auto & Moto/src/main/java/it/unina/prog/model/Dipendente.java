package it.unina.prog.model;

/**
 * Modello di dominio per il Dipendente.
 * Rappresenta un dipendente del concessionario (venditore, meccanico, admin, etc.)
 * con:
 * - id univoco
 * - dati personali (nome, ruolo)
 * - password per l'autenticazione
 * 
 * I campi sono final per immutabilità: una volta creato un Dipendente, non cambia.
 */
public class Dipendente {
    private final int id;
    private final String nome;
    private final String ruolo;
    private final String password;

    /**
     * Crea un'istanza di Dipendente (immutabile).
     * @param id id univoco del dipendente (PK nel database)
     * @param nome nome del dipendente
     * @param ruolo ruolo/qualifica (es. "Venditore", "Responsabile Vendite")
     * @param password password hash per autenticazione
     */
    public Dipendente(int id, String nome, String ruolo, String password) {
        this.id = id;
        this.nome = nome;
        this.ruolo = ruolo;
        this.password = password;
    }

    /**
     * @return id univoco del dipendente
     */
    public int getId() {
        return id;
    }

    /**
     * @return nome del dipendente
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return ruolo/qualifica del dipendente
     */
    public String getRuolo() {
        return ruolo;
    }

    /**
     * @return password hash del dipendente (per autenticazione)
     */
    public String getPassword() {
        return password;
    }
}
