package main.ValidationModule;

public class PeselValidation {
    //Walidacja PESELu - 11 cyfr
    public static boolean validatePesel(String pesel) {
        if (pesel == null || pesel.length() != 11 || !pesel.matches("\\d+")) {
            return false;
        }
        //Walidacja cyfry kontrolnej
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3, 1};
        int sum = 0;

        for (int i = 0; i < 11; i++) {
            sum += Integer.parseInt(String.valueOf(pesel.charAt(i))) * weights[i];
        }

        return sum % 10 == 0;

    }
}
