package main.ValidationModule;

public class NameValidation {
    //Walidacja nazwy/imienia/nazwiska - sprawdzenie długości oraz zawartości
    public static boolean validateName(String name) {
        if (name == null || name.length() > 50) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
}
