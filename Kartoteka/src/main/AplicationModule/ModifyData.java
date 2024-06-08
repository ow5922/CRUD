package main.AplicationModule;

import main.ValidationModule.DateValidation;
import main.ValidationModule.IdCardValidation;
import main.ValidationModule.NameValidation;
import main.ValidationModule.PeselValidation;

import javax.swing.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyData {

    static String url = Properties.url;
    static String user = Properties.user;
    static String password = Properties.password;
    static String cancel = Properties.cancel;


    //Funckaj dodająca nową osobę do tablicy clients
    public static void addSingleData() {
        //Nazwisko
        String surname = JOptionPane.showInputDialog("Podaj nazwisko:");
        //Anulowanie
        if(surname==null){
            JOptionPane.showMessageDialog(null, cancel);
            return;
        }
        //Walidacja
        boolean isValid = NameValidation.validateName(surname);
        while (!isValid) {
            surname = JOptionPane.showInputDialog("Błędny format nazwiska. Podaj nazwisko jeszcze raz :");
            //Anulowanie
            if(surname==null){
                JOptionPane.showMessageDialog(null, cancel);
                return;
            }
            isValid = NameValidation.validateName(surname);
        }

        //Imię
        String firstname = JOptionPane.showInputDialog("Podaj imię:");
        //Anulowanie
        if(firstname==null){
            JOptionPane.showMessageDialog(null, cancel);
            return;
        }
        //Walidacja
        isValid = NameValidation.validateName(firstname);
        while (!isValid) {
            firstname = JOptionPane.showInputDialog("Błędny format imienia. Podaj imię jeszcze raz :");
            //Anulowanie
            if(surname==null){
                JOptionPane.showMessageDialog(null, cancel);
                return;
            }
            isValid = NameValidation.validateName(firstname);
        }

        //Data urodzenia
        String tempdateofBirth = JOptionPane.showInputDialog("Podaj datę :");
        //Anulowanie
        if(tempdateofBirth==null){
            JOptionPane.showMessageDialog(null, cancel);
            return;
        }
        //Walidacja
        isValid = DateValidation.validateDate(tempdateofBirth);
        while (!isValid) {
            tempdateofBirth = JOptionPane.showInputDialog("Błędny format daty urodzenia. Podaj datę w formacie RRRR-MM-DD :");
            //Anulowanie
            if(surname==null){
                JOptionPane.showMessageDialog(null, cancel);
                return;
            }
            isValid = DateValidation.validateDate(tempdateofBirth);
        }
        Date dateofBirth = Date.valueOf(tempdateofBirth);

        //Pesel
        String temppesel = JOptionPane.showInputDialog("Podaj PESEL :");
        //Anulowanie
        if(temppesel==null){
            JOptionPane.showMessageDialog(null, cancel);
            return;
        }
        //Walidacja
        isValid = PeselValidation.validatePesel(temppesel);
        while (!isValid) {
            temppesel = JOptionPane.showInputDialog("Błędny PESEL. Podaj PESEL jeszcze raz :");
            //Anulowanie
            if(surname==null){
                JOptionPane.showMessageDialog(null, cancel);
                return;
            }
            isValid = PeselValidation.validatePesel(temppesel);
        }
        long pesel = Long.parseLong(temppesel);

        //Dowód
        String id_card = JOptionPane.showInputDialog("Podaj nr dowodu :");
        //Anulowanie
        if(id_card==null){
            JOptionPane.showMessageDialog(null, cancel);
            return;
        }
        //Walidacja
        isValid = IdCardValidation.validateIdCard(id_card);
        while (!isValid) {
            id_card = JOptionPane.showInputDialog("Błędny format dowodu. Podaj dowód w formacie ABC123456 :");
            //Anulowanie
            if(surname==null){
                JOptionPane.showMessageDialog(null, cancel);
                return;
            }
            isValid = IdCardValidation.validateIdCard(id_card);
        }
        //Dodanie danych do bazy
        String sqlQuery = "INSERT INTO clients (cl_surname, cl_firstname, date_of_birth, pesel, id_card) VALUES (?, ?, ?, ?, ?)";
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            preparedStatement.setString(1, surname);
            preparedStatement.setString(2, firstname);
            preparedStatement.setDate(3, dateofBirth);
            preparedStatement.setLong(4, pesel);
            preparedStatement.setString(5, id_card);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Nowa pozycja została dodana do bazy danych.");
            } else {
                JOptionPane.showMessageDialog(null, "Nie udało się dodać nowej pozycji do bazy danych.");
            }
        } catch (SQLException e) {
            Logger.getLogger(ModifyData.class.getName()).log(Level.SEVERE, "Wystąpił błąd podczas operacji na bazie danych.", e);
            JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas operacji na bazie danych.");
        }
    }

    //Funckja nadpisująca istniejace dane - pojedyncze pole lub cały wiersz
    public static void updateDataBySurname() {

        String sname = JOptionPane.showInputDialog("Podaj nazwisko:");

        String sqlQuery = "SELECT * FROM clients where cl_surname = ?";
        String updateAllQuery = "UPDATE clients SET cl_id=?, cl_surname=?, cl_firstname=?, date_of_birth=?, pesel=?, id_card=? WHERE cl_id = ?";
        String updateIdQuery = "UPDATE clients SET cl_id=? WHERE cl_id = ?";
        String updateSurnameQuery = "UPDATE clients SET cl_surname=? WHERE cl_id = ?";
        String updateFirstnameQuery = "UPDATE clients SET cl_firstname=? WHERE cl_id = ?";
        String updateDateQuery = "UPDATE clients SET date_of_birth=? WHERE cl_id = ?";
        String updatePeselQuery = "UPDATE clients SET pesel=? WHERE cl_id = ?";
        String updateIdcardQuery = "UPDATE clients SET id_card=? WHERE cl_id = ?";
        boolean foundData = false;
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                PreparedStatement updateAllStatement = connection.prepareStatement(updateAllQuery);
                PreparedStatement updateIdStatement = connection.prepareStatement(updateIdQuery);
                PreparedStatement updateSurnameStatement = connection.prepareStatement(updateSurnameQuery);
                PreparedStatement updateFirstnameStatement = connection.prepareStatement(updateFirstnameQuery);
                PreparedStatement updateDateStatement = connection.prepareStatement(updateDateQuery);
                PreparedStatement updatePeselStatement = connection.prepareStatement(updatePeselQuery);
                PreparedStatement updateIdcardStatement = connection.prepareStatement(updateIdcardQuery)
        ) {
            preparedStatement.setString(1, sname);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Pobranie danych z kolumn tabeli
                    int cl_id = resultSet.getInt("cl_id");
                    String surname = resultSet.getString("cl_surname");
                    String firstname = resultSet.getString("cl_firstname");
                    Date dateOfBirth = resultSet.getDate("date_of_birth");
                    long pesel = resultSet.getLong("pesel");
                    String id_card = resultSet.getString("id_card");

                    JTextField field1 = new JTextField();
                    JTextField field2 = new JTextField();
                    JTextField field3 = new JTextField();
                    JTextField field4 = new JTextField();
                    JTextField field5 = new JTextField();
                    JTextField field6 = new JTextField();
                    JLabel label1 = new JLabel("ID (" + cl_id + ") ");
                    JLabel label2 = new JLabel("Nazwisko (" + surname + ") ");
                    JLabel label3 = new JLabel("Imię (" + firstname + ") ");
                    JLabel label4 = new JLabel("Data urodzienie (" + dateOfBirth + ") ");
                    JLabel label5 = new JLabel("PESEL (" + pesel + ") ");
                    JLabel label6 = new JLabel("Nr dowodu (" + id_card + ") ");
                    foundData = true;
                    Object[] options = {"Edytuj wszystko", "Edytuj ID", "Edytuj nazwisko", "Edytuj imię", "Edytuj datę urodzenia", "Edytuj PESEL", "Edytuj nr dowodu"};
                    int choice = JOptionPane.showOptionDialog(null, "Którą opcję edycji chcesz wybrać", "Edycja danych", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (choice == 0) {
                        Object[] message = {
                                label1, field1,
                                label2, field2,
                                label3, field3,
                                label4, field4,
                                label5, field5,
                                label6, field6
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "Wprowadź dane", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            int newId = Integer.parseInt(field1.getText());
                            String newSurname = field2.getText();
                            //Walidacja
                            boolean isValid = NameValidation.validateName(newSurname);
                            while (!isValid) {
                                newSurname = JOptionPane.showInputDialog("Błędny format nazwiska. Podaj nazwisko jeszcze raz :");
                                //Anulowanie
                                if(surname==null){
                                    JOptionPane.showMessageDialog(null, cancel);
                                    return;
                                }
                                isValid = NameValidation.validateName(newSurname);
                            }
                            String newFirstname = field3.getText();
                            //Walidacja
                            isValid = NameValidation.validateName(newFirstname);
                            while (!isValid) {
                                newFirstname = JOptionPane.showInputDialog("Błędny format imienia. Podaj imię jeszcze raz :");
                                //Anulowanie
                                if(surname==null){
                                    JOptionPane.showMessageDialog(null, cancel);
                                    return;
                                }
                                isValid = NameValidation.validateName(newFirstname);
                            }
                            String tempdateofBirth = field4.getText();
                            //Walidacja
                            isValid = DateValidation.validateDate(tempdateofBirth);
                            while (!isValid) {
                                tempdateofBirth = JOptionPane.showInputDialog("Błędny format daty urodzenia. Podaj datę w formacie RRRR-MM-DD :");
                                //Anulowanie
                                if(surname==null){
                                    JOptionPane.showMessageDialog(null, cancel);
                                    return;
                                }
                                isValid = DateValidation.validateDate(tempdateofBirth);
                            }
                            Date newDate = Date.valueOf(tempdateofBirth);
                            String temppesel = field5.getText();
                            //Walidacja
                            isValid = PeselValidation.validatePesel(temppesel);
                            while (!isValid) {
                                temppesel = JOptionPane.showInputDialog("Błędny PESEL. Podaj PESEL jeszcze raz :");;
                                //Anulowanie
                                if(surname==null){
                                    JOptionPane.showMessageDialog(null, cancel);
                                    return;
                                }
                                isValid = PeselValidation.validatePesel(temppesel);
                            }
                            long newPesel = Long.parseLong(temppesel);

                            String newIdcard = field6.getText();
                            //Walidacja
                            isValid = IdCardValidation.validateIdCard(newIdcard);
                            while (!isValid) {
                                newIdcard = JOptionPane.showInputDialog("Błędny format dowodu. Podaj dowód w formacie ABC123456 :");
                                //Anulowanie
                                if(surname==null){
                                    JOptionPane.showMessageDialog(null, cancel);
                                    return;
                                }
                                isValid = IdCardValidation.validateIdCard(newIdcard);
                            }
                            updateAllStatement.setInt(1, newId);
                            updateAllStatement.setString(2, newSurname);
                            updateAllStatement.setString(3, newFirstname);
                            updateAllStatement.setDate(4, newDate);
                            updateAllStatement.setLong(5, newPesel);
                            updateAllStatement.setString(6, newIdcard);
                            updateAllStatement.setInt(7, cl_id);
                            updateAllStatement.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Dane użytkownika " + firstname + " " + surname + " zostały pomyślnie zmienione na " + newFirstname + " " + newSurname);

                        }
                    } else if (choice == 1) {
                        Object[] message = {
                                label1, field1
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "Wprowadź dane", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            int newId = Integer.parseInt(field1.getText());
                            updateIdStatement.setInt(1, newId);
                            updateIdStatement.setInt(2, cl_id);
                            updateIdStatement.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Dane użytkownika " + firstname + " " + surname + " zostały pomyślnie zmienione");
                        }
                    } else if (choice == 2) {
                        Object[] message = {
                                label2, field2
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "Wprowadź dane", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            String newSurname = field2.getText();
                            boolean isValid = NameValidation.validateName(newSurname);
                            while (!isValid) {
                                newSurname = JOptionPane.showInputDialog("Błędny format nazwiska. Podaj nazwisko jeszcze raz :");
                                isValid = NameValidation.validateName(newSurname);
                            }
                            updateSurnameStatement.setString(1, newSurname);
                            updateSurnameStatement.setInt(2, cl_id);
                            updateSurnameStatement.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Dane użytkownika " + firstname + " " + surname + " zostały pomyślnie zmienione");
                        }
                    } else if (choice == 3) {
                        Object[] message = {
                                label3, field3
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "Wprowadź dane", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            String newFirstname = field3.getText();
                            //Walidacja
                            boolean isValid = NameValidation.validateName(newFirstname);
                            while (!isValid) {
                                newFirstname = JOptionPane.showInputDialog("Błędny format imienia. Podaj imię jeszcze raz :");
                                //Anulowanie
                                if(surname==null){
                                    JOptionPane.showMessageDialog(null, cancel);
                                    return;
                                }
                                isValid = NameValidation.validateName(newFirstname);
                            }
                            updateFirstnameStatement.setString(1, newFirstname);
                            updateFirstnameStatement.setInt(2, cl_id);
                            updateFirstnameStatement.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Dane użytkownika " + firstname + " " + surname + " zostały pomyślnie zmienione");
                        }
                    } else if (choice == 4) {
                        Object[] message = {
                                label4, field4
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "Wprowadź dane", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            String tempdateofBirth = field4.getText();
                            //Walidacja
                            boolean isValid = DateValidation.validateDate(tempdateofBirth);
                            while (!isValid) {
                                tempdateofBirth = JOptionPane.showInputDialog("Błędny format daty urodzenia. Podaj datę w formacie RRRR-MM-DD :");
                                //Anulowanie
                                if(surname==null){
                                    JOptionPane.showMessageDialog(null, cancel);
                                    return;
                                }
                                isValid = DateValidation.validateDate(tempdateofBirth);
                            }
                            Date newDate = Date.valueOf(tempdateofBirth);
                            updateDateStatement.setDate(1, newDate);
                            updateDateStatement.setInt(2, cl_id);
                            updateDateStatement.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Dane użytkownika " + firstname + " " + surname + " zostały pomyślnie zmienione");
                        }
                    } else if (choice == 5) {
                        Object[] message = {
                                label5, field5
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "Wprowadź dane", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            String temppesel = field5.getText();
                            //Walidacja
                            boolean isValid = PeselValidation.validatePesel(temppesel);
                            while (!isValid) {
                                temppesel = JOptionPane.showInputDialog("Błędny PESEL. Podaj PESEL jeszcze raz :");;
                                //Anulowanie
                                if(surname==null){
                                    JOptionPane.showMessageDialog(null, cancel);
                                    return;
                                }
                                isValid = PeselValidation.validatePesel(temppesel);
                            }
                            long newPesel = Long.parseLong(temppesel);
                            updatePeselStatement.setLong(1, newPesel);
                            updatePeselStatement.setInt(2, cl_id);
                            updatePeselStatement.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Dane użytkownika " + firstname + " " + surname + " zostały pomyślnie zmienione");
                        }
                    } else if (choice == 6) {
                        Object[] message = {
                                label6, field6
                        };
                        int option = JOptionPane.showConfirmDialog(null, message, "Wprowadź dane", JOptionPane.OK_CANCEL_OPTION);
                        if (option == JOptionPane.OK_OPTION) {
                            String newIdcard = field2.getText();
                            //Walidacja
                            boolean isValid = IdCardValidation.validateIdCard(newIdcard);
                            while (!isValid) {
                                newIdcard = JOptionPane.showInputDialog("Błędny format dowodu. Podaj dowód w formacie ABC123456 :");
                                //Anulowanie
                                if(surname==null){
                                    JOptionPane.showMessageDialog(null, cancel);
                                    return;
                                }
                                isValid = IdCardValidation.validateIdCard(newIdcard);
                            }
                            updateIdcardStatement.setString(1, newIdcard);
                            updateIdcardStatement.setInt(2, cl_id);
                            updateIdcardStatement.executeUpdate();
                            JOptionPane.showMessageDialog(null, "Dane użytkownika " + firstname + " " + surname + " zostały pomyślnie zmienione");
                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Dane użytkownika " + firstname + " " + surname + " nie zostały zmienione");
                    }
                }
                if (!foundData) {
                    JOptionPane.showMessageDialog(null, "Brak danych dla podanego nazwiska: " + sname);
                }
            } catch (SQLException e) {
                Logger.getLogger(ModifyData.class.getName()).log(Level.SEVERE, "Wystąpił błąd podczas operacji na bazie danych.", e);
                JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas operacji na bazie danych.");
            }
        } catch (SQLException e) {
            Logger.getLogger(ModifyData.class.getName()).log(Level.SEVERE, "Wystąpił błąd podczas operacji na bazie danych.", e);
            JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas operacji na bazie danych.");
        }
    }
    //Funckja usuwająca osobę
    public static void deleteDataBySurname() {

        String sname = JOptionPane.showInputDialog("Podaj nazwisko:");

        String sqlQuery = "SELECT * FROM clients where cl_surname = ?";
        String deleteQuery = "DELETE FROM clients WHERE cl_id = ?";
        boolean foundData = false;
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setString(1, sname);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Pobranie danych z kolumn tabeli
                    int cl_id = resultSet.getInt("cl_id");
                    String surname = resultSet.getString("cl_surname");
                    String firstname = resultSet.getString("cl_firstname");
                    long pesel = resultSet.getLong("pesel");

                    foundData = true;
                    int choice = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz usunąć użytkownika " + firstname + " " + surname + " o PESELu: " + pesel + " z kartoteki?", "Usuwanie danych", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        deleteStatement.setInt(1, cl_id);
                        deleteStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Dane użytkownika " + firstname + " " + surname + " zostały pomyślnie usunięte.");
                    }
                }
            }
            if (!foundData) {
                JOptionPane.showMessageDialog(null, "Brak danych dla podanego nazwiska: " + sname);
            }
        } catch (SQLException e) {
            Logger.getLogger(ModifyData.class.getName()).log(Level.SEVERE, "Wystąpił błąd podczas operacji na bazie danych.", e);
            JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas operacji na bazie danych.");
        }
    }
}
