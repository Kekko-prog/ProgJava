package it.unina.prog.model;

public class Dipendente {
    private final int id;
    private final String nome;
    private final String ruolo;
    private final String password;

    public Dipendente(int id, String nome, String ruolo, String password) {
        this.id = id;
        this.nome = nome;
        this.ruolo = ruolo;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRuolo() {
        return ruolo;
    }

    public String getPassword() {
        return password;
    }
}
