package it.unina.prog.ui.validation;

import java.time.LocalDate;

public final class InputValidator {
    private InputValidator() {
    }

    public static boolean isValidEmail(String email) {
        return email != null
                && !email.isBlank()
                && !email.matches(".*\\s+.*")
                && email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public static String normalizePlate(String plate) {
        return plate == null ? "" : plate.trim().toUpperCase();
    }

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

    public static LocalDate parseDateOrThrow(String dateInput) {
        try {
            return LocalDate.parse(dateInput);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Formato data non valido: usa YYYY-MM-DD");
        }
    }

    public static void requireTodayOrFuture(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data non valida: puoi selezionare solo oggi o una data futura");
        }
    }
}
