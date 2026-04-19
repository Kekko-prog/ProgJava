package it.unina.prog.ui.model;

import it.unina.prog.model.Cliente;

public final class UiModels {
    private UiModels() {
    }

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

    public static class ClienteLogin {
        public final Cliente cliente;
        public final String password;

        public ClienteLogin(Cliente cliente, String password) {
            this.cliente = cliente;
            this.password = password;
        }
    }
}
