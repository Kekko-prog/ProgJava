package it.unina.prog.gui.validation;

import java.time.LocalDate;

/**
 * Classe di utilità per la validazione dell'input utente.
 * Fornisce metodi statici per:
 * - Validazione email, telefono, targa
 * - Normalizzazione dati (uppercase, trim)
 * - Parse e validazione date
 * - Vincoli di business (es. solo date future per test drive)
 */
public final class InputValidator {
    /**
     * Costruttore privato: utility class con soli metodi statici.
     */
    private InputValidator() {
    }

    /**
     * Valida formato email: deve contenere @ e . e non spazi.
     * Regex semplice, non RFC 5322 completo (buono per UI).
     * @param email stringa da validare
     * @return true se formato email valido
     */
    public static boolean isValidEmail(String email) {
        return email != null
                && !email.isBlank()
                && !email.matches(".*\\s+.*")
                && email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    /**
     * Valida numero di telefono: deve essere esattamente 10 cifre.
     * Regex semplice, specializzato per Italia o simile.
     * @param phone stringa da validare
     * @return true se esattamente 10 cifre
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    /**
     * Normalizza una targa: trim spazi e converte a maiuscolo.
     * @param plate targa grezza
     * @return targa normalizzata o stringa vuota se null
     */
    public static String normalizePlate(String plate) {
        return plate == null ? "" : plate.trim().toUpperCase();
    }

    /**
     * Valida formato targa: 5-10 caratteri alfanumerici, niente spazi.
     * Non controlla univocità (quella è competenza del DB).
     * @param plate targa da validare
     * @return true se formato valido
     */
    public static boolean isValidPlate(String plate) {
        if (plate == null) {
            return false;
        }
        String p = plate.trim();
        return !p.isEmpty()
                && !p.matches(".*\\s+.*")
                && p.matches("^[A-Za-z0-9]+$")
                && p.length() >= 5
                && p.length() <= 10;
    }

    /**
     * Parse una data dal formato ISO (YYYY-MM-DD).
     * Solleva IllegalArgumentException se formato non valido.
     * @param dateInput stringa data
     * @return LocalDate parsato
     * @throws IllegalArgumentException se formato non riconosciuto
     */
    public static LocalDate parseDateOrThrow(String dateInput) {
        try {
            return LocalDate.parse(dateInput);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Formato data non valido: usa YYYY-MM-DD");
        }
    }

    /**
     * Verifica che una data sia oggi o nel futuro.
     * Usato per validare date di prenotazione test drive.
     * @param date data da controllare
     * @throws IllegalArgumentException se data è nel passato
     */
    public static void requireTodayOrFuture(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data non valida: puoi selezionare solo oggi o una data futura");
        }
    }
}