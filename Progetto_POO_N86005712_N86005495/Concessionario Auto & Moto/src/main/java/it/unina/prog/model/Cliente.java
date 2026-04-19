package it.unina.prog.model;

public class Cliente {
    private int id;
    private String nome;
    private String tipo;
    private String email;
    private String telefono;

    public Cliente(int id, String nome, String tipo, String email, String telefono) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.email = email;
        this.telefono = telefono;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
}
