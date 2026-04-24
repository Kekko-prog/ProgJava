package it.unina.prog.gui.model;

import it.unina.prog.model.Cliente;

/**
 * Contenitore di classi di utilità per la GUI.
 * Definisce modelli semplificati usati nei ComboBox e liste della UI.
 * A differenza dei modelli di dominio (entity), questi sono pensati per la visualizzazione.
 */
public final class UiModels {
    /**
     * Costruttore privato: utility class.
     */
    private UiModels() {
    }

    /**
     * Elemento cliente per visualizzazione in ComboBox.
     * Contiene id e nome, mostra "nome (id)" quando selezionato.
     */
    public static class ClienteItem {
        public final int id;
        public final String nome;

        public ClienteItem(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }

        @Override
        public String toString() {
            return nome + " (" + id + ")";
        }
    }

    /**
     * Elemento veicolo per visualizzazione in ComboBox.
     * Mostra "targa - modello" permettendo selezione semplice.
     * Include il prezzo per operazioni di vendita.
     */
    public static class VeicoloItem {
        public final String targa;
        public final String modello;
        public final double prezzo;

        public VeicoloItem(String targa, String modello, double prezzo) {
            this.targa = targa;
            this.modello = modello;
            this.prezzo = prezzo;
        }

        @Override
        public String toString() {
            return targa + " - " + modello;
        }
    }

    /**
     * Wrapper per mantenere sia i dati del cliente che la password inserita.
     * Usato temporaneamente durante process login.
     */
    public static class ClienteLogin {
        public final Cliente cliente;
        public final String password;

        public ClienteLogin(Cliente cliente, String password) {
            this.cliente = cliente;
            this.password = password;
        }
    }
}