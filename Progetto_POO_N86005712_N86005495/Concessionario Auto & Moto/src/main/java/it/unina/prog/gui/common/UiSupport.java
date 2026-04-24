package it.unina.prog.gui.common;

import javax.swing.*;
import java.awt.*;

/**
 * Classe di utilità per la GUI Swing.
 * Fornisce helper per:
 * - Styling di pannelli (titoli bordi, colori)
 * - Visualizzazione di messaggi di errore
 * - Traduzione di eccezioni database in messaggi user-friendly
 */
public final class UiSupport {
    /**
     * Costruttore privato: questa è un'utility class con soli metodi statici.
     */
    private UiSupport() {
    }

    /**
     * Aggiunge un titolo e styling a un pannello.
     * Applica border titolato e colore di sfondo standard.
     * @param panel pannello da decorare
     * @param title testo del titolo
     * @return lo stesso pannello (modificato)
     */
    public static JPanel wrapTitled(JPanel panel, String title) {
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBackground(new Color(245, 245, 245));
        return panel;
    }

    /**
     * Mostra una finestra di errore all'utente.
     * Converte l'eccezione in un messaggio user-friendly tramite toUserMessage().
     * @param parent componente padre (per centrare la dialog)
     * @param ex eccezione da visualizzare
     */
    public static void showErr(Component parent, Exception ex) {
        JOptionPane.showMessageDialog(parent, "Errore: " + toUserMessage(ex), "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Traduce messaggi di errore tecnici in testo user-friendly.
     * Individua pattern di errore comuni (FK violati, check constraint, etc.)
     * e li converte in messaggi comprensibili per l'utente finale.
     * @param ex eccezione da tradurre
     * @return messaggio personalizzato o messaggio originale se non riconosciuto
     */
    public static String toUserMessage(Throwable ex) {
        String message = ex != null && ex.getMessage() != null ? ex.getMessage() : "Errore imprevisto";
        String lower = message.toLowerCase();

        if (lower.contains("veicolo_marca_fkey") || (lower.contains("tabella \"marca\"") && lower.contains("chiave"))) {
            return "Marca non valida: inserisci una marca esistente nella tabella Marche.";
        }

        if (lower.contains("veicolo_pkey") || (lower.contains("duplicate key") && lower.contains("targa"))) {
            return "Targa gia presente: usa una targa diversa.";
        }

        if (lower.contains("check_testdrive_limite") ||
                lower.contains("limite massimo di 3 test drive al mese raggiunto") ||
                (lower.contains("test drive") && lower.contains("limite massimo"))) {
            return "Limite raggiunto: questo cliente ha gia effettuato 3 test drive nel mese corrente.";
        }

        return message;
    }
}