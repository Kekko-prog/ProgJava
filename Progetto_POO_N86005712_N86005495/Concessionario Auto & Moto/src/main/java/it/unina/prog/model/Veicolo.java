package it.unina.prog.model;

public class Veicolo {
    private String targa;
    private String marca;
    private String modello;
    private String tipo;
    private double prezzo;
    private String stato;

    public Veicolo(String targa, String marca, String modello, String tipo, double prezzo, String stato) {
        this.targa = targa;
        this.marca = marca;
        this.modello = modello;
        this.tipo = tipo;
        this.prezzo = prezzo;
        this.stato = stato;
    }

    public String getTarga() { return targa; }
    public String getMarca() { return marca; }
    public String getModello() { return modello; }
    public String getTipo() { return tipo; }
    public double getPrezzo() { return prezzo; }
    public String getStato() { return stato; }
}
