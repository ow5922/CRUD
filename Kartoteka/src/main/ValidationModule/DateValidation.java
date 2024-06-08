package main.ValidationModule;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateValidation {

    public static boolean validateDate(String date) {
        try {

            // Walidacja czy urodzenie nie jest w przyszłości
            // LocalDate wymusza format RRRR-MM-DD
            LocalDate dateofbirth = LocalDate.parse(date);
            LocalDate today = LocalDate.now();
            return !dateofbirth.isAfter(today);

        } catch (DateTimeParseException e) {
            // Wyjątek gdy błędna walidacja
            return false;
        }
    }


}
