package it.unina.prog.model;

/**
 * Modello di dominio per il Cliente.
 * Rappresenta un cliente del concessionario con:
 * - id univoco nel database
 * - dati personali (nome, email, telefono)
 * - tipo di cliente (es. "Privato", "Azienda")
 * 
 * Nota: la password NON è inclusa nel modello per motivi di sicurezza.
 * Viene gestita separatamente dal layer di autenticazione.
 */
public class Cliente {
    private int id;
    private String nome;
    private String tipo;
    private String email;
    private String telefono;

    /**
     * Crea un'istanza di Cliente con tutti i dati.
     * @param id id univoco del cliente (PK nel database)
     * @param nome nome completo del cliente
     * @param tipo categoria di cliente (es. "Privato")
     * @param email email di contatto
     * @param telefono numero di telefono
     */
    public Cliente(int id, String nome, String tipo, String email, String telefono) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.email = email;
        this.telefono = telefono;
    }

    /**
     * @return id univoco del cliente
     */
    public int getId() { return id; }
    
    /**
     * @return nome completo del cliente
     */
    public String getNome() { return nome; }
    
    /**
     * @return tipo di cliente (es. "Privato", "Azienda")
     */
    public String getTipo() { return tipo; }
    
    /**
     * @return email di contatto del cliente
     */
    public String getEmail() { return email; }
    
    /**
     * @return numero di telefono del cliente
     */
    public String getTelefono() { return telefono; }
}
