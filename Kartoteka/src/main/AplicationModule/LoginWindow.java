//Formatka logowania
package main.AplicationModule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Runnable onSuccessCallback;

    public String type;
    //String url = "jdbc:mysql://localhost:3306/kartoteka";
    String url=Properties.url;
    String user = Properties.user;
    String password = Properties.password;

    public void displayLoginWindow(Runnable onSuccessCallback) {
        this.onSuccessCallback = onSuccessCallback;
        setTitle("Logowanie");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("  Nazwa użytkownika:");
        add(usernameLabel);
        usernameField = new JTextField();
        add(usernameField);

        JLabel passwordLabel = new JLabel("  Hasło:");
        add(passwordLabel);
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginButton = new JButton("Zaloguj");
        add(loginButton);
        JButton cancelButton = new JButton("Anuluj");
        add(cancelButton);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                String login = usernameField.getText();
                String pass = new String(passwordField.getPassword());

                try (Connection connection = DriverManager.getConnection(url, user, password)) {
                    String sqlQuery = "SELECT oper_type FROM operators WHERE oper_name = ? AND oper_password = ?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
                        preparedStatement.setString(1, login);
                        preparedStatement.setString(2, pass);

                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                // Przypisanie typu operatora do zmiennej type
                                type = resultSet.getString("oper_type");

                                setVisible(false);
                                if (onSuccessCallback != null) {
                                    onSuccessCallback.run();
                                }
                                return;
                            }
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ModifyData.class.getName()).log(Level.SEVERE, "Wystąpił błąd podczas operacji na bazie danych.", e);
                    JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas operacji na bazie danych.");
                }

                // Komunikat błędnego logowania
                JOptionPane.showMessageDialog(null, "Błędne dane logowania. Spróbuj ponownie.", "Błąd logowania", JOptionPane.ERROR_MESSAGE);
            }
        });

        setSize(300, 150);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    //Funkcja zwracająca typ operatora
    public String readOperatortype(){
            return type;

    }
}