package survey.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Analytics extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/surveys";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public Analytics() {
        super("Survey Analytics");
        JPanel mainPanel = new JPanel(new GridLayout(0, 1, 0, 50));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String query = "SELECT response_text FROM survey_responses";
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                Map<String, Integer> answerCounts = new HashMap<>();
                int totalAnswers = 0;

                // Count occurrences of each answer
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        String answer = resultSet.getString(i);
                        if (answer != null && !answer.isEmpty()) {
                            answerCounts.put(answer, answerCounts.getOrDefault(answer, 0) + 1);
                            totalAnswers++;
                        }
                    }
                }

                // Create progress bar for each answer
                for (Map.Entry<String, Integer> entry : answerCounts.entrySet()) {
                    String answer = entry.getKey();
                    int count = entry.getValue();
                    double percentage = (count * 100.0) / totalAnswers;

                    JProgressBar progressBar = new JProgressBar(0, 100);
                    progressBar.setString(answer + ": " + String.format("%.1f%%", percentage));
                    progressBar.setStringPainted(true);
                    progressBar.setValue((int) percentage);
                    mainPanel.add(progressBar);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JButton button = new JButton("back");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Button click action
            	dispose();
               new Menu();
            }
        });

        add(mainPanel, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

  
    }
