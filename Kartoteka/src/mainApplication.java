import main.AplicationModule.FileActions;
import main.AplicationModule.LoginWindow;
import main.AplicationModule.ModifyData;
import main.AplicationModule.ReadData;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class mainApplication {
    static String userType;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Uruchom okno logowania
                LoginWindow login = new LoginWindow();
                login.displayLoginWindow(new Runnable() {
                    @Override
                    public void run() {
                        login.readOperatortype();
                        if(login.readOperatortype().equals("A")){
                            userType= String.valueOf('A');
                            openMainWindowForA();
                        } else if (login.readOperatortype().equals("U")){
                            userType= String.valueOf('U');
                            openMainWindowForA();
                        } else {
                            userType= String.valueOf('D');
                            openMainWindowForA();
                        }


                    }
                });
            }
        });
    }
    private static void openMainWindowForA() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        // Stwórz nową ramkę
        JFrame frame = new JFrame("Program do zarządzania danymi osobowymi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight);
        frame.setResizable(false);

        // Stwórz panel do przechowywania komponentów
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Rysuj tło zajmujące cały ekran
                g.setColor(Color.WHITE); // Tutaj możesz zmienić kolor tła
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPane.setLayout(new BorderLayout());

        // Stwórz panel do umieszczenia przycisków
        JPanel panel = new JPanel(new GridBagLayout());
        //panel.setLayout(new GridLayout(9, 1));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 5, 5, 5); // odstęp między przyciskami

        // Dodaj przyciski do panelu
        JButton[] buttons = new JButton[8];
        buttons[0] = new JButton("Odczyt danych po nazwisku");
        buttons[1] = new JButton("Odczyt wszystkich danych");
        buttons[2] = new JButton("Dodanie nowej osoby");
        buttons[3] = new JButton("Edycja istniejącej osoby");
        buttons[4] = new JButton("Usunięcie istniejącej osoby");
        buttons[5] = new JButton("Zapisanie wszystkich danych do pliku");
        buttons[6] = new JButton("Zaczytanie danych z pliku");
        buttons[7] = new JButton("Zamknięcie programu");

        Dimension buttonSize = new Dimension(700, 60);
        for (JButton button : buttons) {
            button.setPreferredSize(buttonSize);
            gbc.gridy++;
            panel.add(button, gbc);

        }
        if (Objects.equals(userType, "U")) {
            buttons[2].setEnabled(false);
            buttons[3].setEnabled(false);
            buttons[4].setEnabled(false);
            buttons[6].setEnabled(false);
        } else if (Objects.equals(userType, "D")) {
            buttons[1].setEnabled(false);
            buttons[2].setEnabled(false);
            buttons[3].setEnabled(false);
            buttons[4].setEnabled(false);
            buttons[5].setEnabled(false);
            buttons[6].setEnabled(false);
        }

            // Dodaj akcje do przycisków
            buttons[0].addActionListener(e -> ReadData.readDataBySurname());
            buttons[1].addActionListener(e -> {
                DefaultTableModel model = new DefaultTableModel();
                JTable table = new JTable(model);
                model.addColumn("ID");
                model.addColumn("First Name");
                model.addColumn("Last Name");
                model.addColumn("Date Of Birth");
                model.addColumn("PESEL");
                model.addColumn("ID Card");
                ReadData.readAllData(model);
                JPanel tablePanel = new JPanel(new BorderLayout());
                tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
                JFrame tableFrame = new JFrame("Pełna lista");
                tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                tableFrame.add(tablePanel);
                tableFrame.pack();
                tableFrame.setLocationRelativeTo(frame);
                tableFrame.setVisible(true);
            });
            buttons[2].addActionListener(e -> ModifyData.addSingleData());
            buttons[3].addActionListener(e -> ModifyData.updateDataBySurname());
            buttons[4].addActionListener(e -> ModifyData.deleteDataBySurname());
            buttons[5].addActionListener(e -> FileActions.writeAllDataToFile());
            buttons[6].addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki CSV", "csv");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    FileActions.importDataFromFile(filePath);
                } else {
                    JOptionPane.showMessageDialog(null, "Anulowano wybór pliku.");
                }
            });
            buttons[7].addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(frame, "Czy na pewno chcesz zamknąć aplikację?", "Zamknij aplikację", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            });

            JLabel label = new JLabel(" Wybierz operację, którą chcesz wykonać:");
            label.setFont(label.getFont().deriveFont(16f));
            contentPane.add(label, BorderLayout.NORTH);
            contentPane.add(panel, BorderLayout.CENTER);

            // Dodaj panel z komponentami do ramki
            frame.setContentPane(contentPane);

            // Wyśrodkuj ramkę na ekranie
            centerWindow(frame);

            // Ustaw widoczność ramki
            frame.setVisible(true);
        }


    private static void centerWindow(JFrame frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) /2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) /2);
        frame.setLocation(x, y);
    }


    private static void centerWindow(Window window) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - window.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - window.getHeight()) / 2);
        window.setLocation(x, y);
    }

}