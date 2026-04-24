package it.unina.prog.model;

/**
 * Modello di dominio per il Veicolo.
 * Rappresenta un veicolo nel catalogo del concessionario:
 * - auto, moto, furgoni, etc.
 * - con dati di identificazione (targa, marca, modello)
 * - prezzo di catalogo
 * - stato di disponibilità (es. "Disponibile", "Venduto", "In manutenzione")
 */
public class Veicolo {
    private String targa;
    private String marca;
    private String modello;
    private String tipo;
    private double prezzo;
    private String stato;

    /**
     * Crea un'istanza di Veicolo con tutti i dati.
     * @param targa targa del veicolo (PK nel database, identificatore univoco)
     * @param marca marca/casa produttrice (es. "Fiat", "BMW")
     * @param modello modello specifico (es. "500", "X3")
     * @param tipo categoria del veicolo (es. "Auto", "Moto", "Furgone")
     * @param prezzo prezzo di catalogo
     * @param stato stato del veicolo (es. "Disponibile", "Venduto", "In manutenzione")
     */
    public Veicolo(String targa, String marca, String modello, String tipo, double prezzo, String stato) {
        this.targa = targa;
        this.marca = marca;
        this.modello = modello;
        this.tipo = tipo;
        this.prezzo = prezzo;
        this.stato = stato;
    }

    /**
     * @return targa del veicolo (identificatore univoco)
     */
    public String getTarga() { return targa; }
    
    /**
     * @return marca produttrice
     */
    public String getMarca() { return marca; }
    
    /**
     * @return modello del veicolo
     */
    public String getModello() { return modello; }
    
    /**
     * @return categoria di veicolo
     */
    public String getTipo() { return tipo; }
    
    /**
     * @return prezzo di catalogo del veicolo
     */
    public double getPrezzo() { return prezzo; }
    
    /**
     * @return stato di disponibilità del veicolo
     */
    public String getStato() { return stato; }
}
