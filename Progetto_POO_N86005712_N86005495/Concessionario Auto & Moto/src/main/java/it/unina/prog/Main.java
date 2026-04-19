package it.unina.prog;

/**
 * Entry point dell'applicazione.
 * Questo wrapper delega l'avvio alla classe MainApp (Swing).
 */
public class Main {
    public static void main(String[] args) {
        // Richiama la vera implementazione dell'app.
        MainApp.main(args);
    }
}