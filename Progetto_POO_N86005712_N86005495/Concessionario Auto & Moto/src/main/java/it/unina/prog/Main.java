package it.unina.prog;

/**
 * Entry point semplice dell'applicazione.
 * Questo wrapper delega l'avvio alla classe MainApp (Swing).
 * Separare il main puro da MainApp permette di testare la logica di startup in modo clean.
 */
public class Main {
    /**
     * Punto di ingresso dell'intera applicazione.
     * @param args argomenti da linea di comando (non utilizzati al momento)
     */
    public static void main(String[] args) {
        // Richiama la vera implementazione dell'app (startup Swing).
        MainApp.main(args);
    }
}