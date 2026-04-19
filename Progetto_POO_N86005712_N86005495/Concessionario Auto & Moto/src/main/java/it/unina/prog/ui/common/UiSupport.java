package it.unina.prog.ui.common;

import javax.swing.*;
import java.awt.*;

public final class UiSupport {
    private UiSupport() {
    }

    public static JPanel wrapTitled(JPanel panel, String title) {
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setBackground(new Color(245, 245, 245));
        return panel;
    }

    public static void showErr(Component parent, Exception ex) {
        JOptionPane.showMessageDialog(parent, "Errore: " + toUserMessage(ex), "Errore", JOptionPane.ERROR_MESSAGE);
    }

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
