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
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            surveyPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            surveyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            frame.add(new JScrollPane(surveyPanel), BorderLayout.CENTER);

            loadSurveys();

            // Add back button
            JButton backButton = new JButton("Back");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose(); // Dispose current frame
                    new Menu(); // Go back to main menu
                }
            });
            frame.add(backButton, BorderLayout.SOUTH);

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

            while (resultSet.next()) {
                int surveyId = resultSet.getInt("id");
                String surveyTitle = resultSet.getString("title");

                JButton surveyButton = new JButton(surveyTitle);
                surveyButton.setPreferredSize(new Dimension(150, 50));
                surveyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                surveyButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Survey(surveyId);
                    }
                });
                surveyPanel.add(surveyButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Surveylist();
    }
}
