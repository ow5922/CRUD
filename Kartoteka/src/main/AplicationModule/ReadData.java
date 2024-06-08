package main.AplicationModule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.AplicationModule.Properties.cancel;

public class ReadData {

    static String url = Properties.url;
    static String user = Properties.user;
    static String password = Properties.password;

    //Odczytanie i wyświetlenie danych pojedynczej osoby - wyszukanie po nazwisku
    public static void readDataBySurname() {

        String sname = JOptionPane.showInputDialog("Podaj nazwisko:");



        String sqlQuery = "SELECT * FROM clients where cl_surname = ?";

        boolean foundData = false;
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
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

                    if(surname==null){
                        JOptionPane.showMessageDialog(null, cancel);
                        return;
                    }
                    foundData = true;
                    //Wyświetlanie danych w osobnym oknie
                    JOptionPane.showMessageDialog(null, "ID: " + cl_id + "\n" + "First Name: " + firstname + "\n" + "Name: " + surname + "\n" + "Date of birth: " + dateOfBirth + "\n" + "PESEL: " + pesel + "\n" + "ID CARD: " + id_card);

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

    //Wtświetlenie listy wszytkich osób z tablicy clients
    public static void readAllData(DefaultTableModel model) {
        String sqlQuery = "SELECT * FROM clients";
        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {

                int cl_id = resultSet.getInt("cl_id");
                String surname = resultSet.getString("cl_surname");
                String firstname = resultSet.getString("cl_firstname");
                Date dateOfBirth = resultSet.getDate("date_of_birth");
                long pesel = resultSet.getLong("pesel");
                String id_card = resultSet.getString("id_card");

                model.addRow(new Object[]{cl_id, firstname, surname, dateOfBirth, pesel, id_card});
            }
        } catch (SQLException e) {
            Logger.getLogger(ModifyData.class.getName()).log(Level.SEVERE, "Wystąpił błąd podczas operacji na bazie danych.", e);
            JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas operacji na bazie danych.");
        }
    }




}
