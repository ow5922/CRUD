package main.AplicationModule;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileActions {
    static String url = Properties.url;
    static String user = Properties.user;
    static String password = Properties.password;
    //Funkcja zapisująca wszystkie dane użytkownik z tablicy clients do pliku w formacie txt lub csv
    public static void writeAllDataToFile() {
        String downloadPath = System.getProperty("user.home") + "\\Downloads";
        String sqlQuery = "SELECT * FROM clients";
        String txtFilePath = downloadPath + "\\dane.txt";
        String csvFilePath = downloadPath + "\\dane.csv";
        Object[] options = {"Plik CSV", "Plik TXT"};
        int choice = JOptionPane.showOptionDialog(null, "W jakim formacie chcesz pobrać dane?", "Pobranie danych", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            if (choice == 0) {
                try (FileWriter csvWriter = new FileWriter(csvFilePath)) {
                    while (resultSet.next()) {
                        int cl_id = resultSet.getInt("cl_id");
                        String surname = resultSet.getString("cl_surname");
                        String firstname = resultSet.getString("cl_firstname");
                        Date dateOfBirth = resultSet.getDate("date_of_birth");
                        long pesel = resultSet.getLong("pesel");
                        String id_card = resultSet.getString("id_card");
                        csvWriter.write(cl_id + "," + firstname + "," + surname + "," + dateOfBirth + "," + pesel + "," + id_card + "\n");
                    }
                    JOptionPane.showMessageDialog(null, "Dane użytkowników zostały pomyślnie zapisane w lokalizacji " + csvFilePath);
                }
            } else if (choice == 1) {
                try (FileWriter txtWriter = new FileWriter(txtFilePath)) {
                    while (resultSet.next()) {
                        int cl_id = resultSet.getInt("cl_id");
                        String surname = resultSet.getString("cl_surname");
                        String firstname = resultSet.getString("cl_firstname");
                        Date dateOfBirth = resultSet.getDate("date_of_birth");
                        long pesel = resultSet.getLong("pesel");
                        String id_card = resultSet.getString("id_card");
                        txtWriter.write("ID: " + cl_id + ", First Name: " + firstname + ", Name: " + surname + ", Date: " + dateOfBirth + ", PESEL: " + pesel + ", ID Card: " + id_card + "\n");
                    }
                    JOptionPane.showMessageDialog(null, "Dane użytkowników zostały pomyślnie zapisane w lokalizacji " + txtFilePath);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Nie zapisano danych");
            }
        } catch (SQLException | IOException e) {
            Logger.getLogger(ModifyData.class.getName()).log(Level.SEVERE, "Wystąpił błąd podczas operacji na bazie danych.", e);
            JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas operacji na bazie danych.");
        }
    }
    //Funkcja importująca dane do tablicy clients
    public static void importDataFromFile(String filePath) {
        String sqlQuery = "INSERT INTO clients (cl_surname, cl_firstname, date_of_birth, pesel, id_card) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection = DriverManager.getConnection(url, user, password);
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    String surname = data[0].trim();
                    String firstname = data[1].trim();
                    Date dateOfBirth = Date.valueOf(data[2].trim());
                    long pesel = Long.parseLong(data[3].trim());
                    String id_card = data[4].trim();

                    preparedStatement.setString(1, surname);
                    preparedStatement.setString(2, firstname);
                    preparedStatement.setDate(3, dateOfBirth);
                    preparedStatement.setLong(4, pesel);
                    preparedStatement.setString(5, id_card);
                    preparedStatement.executeUpdate();
                } else {
                    JOptionPane.showMessageDialog(null, "Błędny format pliku");
                }
            }
            JOptionPane.showMessageDialog(null, "Dane zaimportowane prawidłowo");
        } catch (SQLException | IOException e) {
            Logger.getLogger(ModifyData.class.getName()).log(Level.SEVERE, "Wystąpił błąd podczas operacji na bazie danych.", e);
            JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas operacji na bazie danych.");
        }
    }
}
