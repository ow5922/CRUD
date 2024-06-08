package main.ValidationModule;

public class IdCardValidation {
    //Walidacja numeru dowodu
    public static boolean validateIdCard(String idcard) {
        if (idcard == null || idcard.length() != 9 || !idcard.matches("[A-Z]{3}[0-9]{6}")) {
            return false;
        }

        return true;

    }
}



