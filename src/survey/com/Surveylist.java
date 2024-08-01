package survey.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Surveylist extends JFrame {
    private Connection connection;
    private JPanel surveyPanel;
    private JFrame frame;

    public Surveylist() {
        try {
            // Establish JDBC connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost/surveys", "root", "");

            // Set up the UI
            frame = new JFrame("Survey List");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            // Panel to hold survey buttons
            surveyPanel = new JPanel(new GridBagLayout());
            surveyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Scroll pane for the survey panel
            JScrollPane scrollPane = new JScrollPane(surveyPanel);
            frame.add(scrollPane, BorderLayout.CENTER);

            // Load surveys from database
            loadSurveys();

            // Back button setup
            JButton backButton = new JButton("Back");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose(); // Dispose current frame
                    new Menu(); // Go back to main menu
                }
            });
            frame.add(backButton, BorderLayout.SOUTH);

            // Display the frame
            frame.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSurveys() {
        try {
            String query = "SELECT id, title FROM survey_master";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 5, 5, 5);

            while (resultSet.next()) {
                int surveyId = resultSet.getInt("id");
                String surveyTitle = resultSet.getString("title");

                JButton surveyButton = new JButton(surveyTitle);
                surveyButton.setPreferredSize(new Dimension(150, 50));
                surveyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                surveyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Survey(surveyId); // Open survey details
                    }
                });

                // Add button to the survey panel
                surveyPanel.add(surveyButton, gbc);
                gbc.gridy++; // Move to the next row
            }

            // Update the panel layout
            surveyPanel.revalidate();
            surveyPanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Surveylist();
            }
        });
    }
}
