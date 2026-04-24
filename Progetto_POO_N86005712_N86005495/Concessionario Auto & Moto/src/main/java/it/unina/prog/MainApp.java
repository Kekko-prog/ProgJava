package it.unina.prog;

import javax.swing.*;

import it.unina.prog.gui.ConcessionarioFrame;

/**
 * Classe di startup dell'applicazione Swing.
 * Responsabile di:
 * - Configurare il look&feel nativo del sistema operativo
 * - Lanciare la creazione della finestra principale su EDT (Event Dispatch Thread)
 * - Gestire il ciclo vita dell'applicazione GUI
 */
public class MainApp {
    /**
     * Punto di ingresso vero dell'applicazione.
     * Tutto il codice Swing deve eseguire su EDT per thread-safety.
     * @param args argomenti non utilizzati
     */
    public static void main(String[] args) {
        // Tutta la UI Swing deve partire sul thread grafico (EDT = Event Dispatch Thread).
        // SwingUtilities.invokeLater garantisce esecuzione sincronizzata.
        SwingUtilities.invokeLater(() -> {
            try {
                // Usa l'aspetto nativo del sistema operativo (Windows, macOS, Linux look&feel).
                // Rende l'applicazione coerente con l'ambiente locale dell'utente.
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Se il Look&Feel non è disponibile, si continua con quello di default.
                // Non è critico, è solo una preferenza di apparenza.
            }
            // Crea e mostra la finestra principale dell'applicazione.
            new ConcessionarioFrame().setVisible(true);
        });
    }
}
