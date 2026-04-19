package it.unina.prog;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import it.unina.prog.ui.ConcessionarioFrame;


public class MainApp {
    public static void main(String[] args) {
        // Tutta la UI Swing deve partire sul thread grafico (EDT).
        SwingUtilities.invokeLater(() -> {
            try {
                // Usa l'aspetto nativo del sistema operativo.
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Se il Look&Feel non è disponibile, si continua con quello di default.
            }
            // Crea e mostra la finestra principale.
            new ConcessionarioFrame().setVisible(true);
        });
    }
}
