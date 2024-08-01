package survey.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Survey extends JFrame {
    private Connection connection;
    private JPanel formPanel;
    private JButton submitButton;
    private int surveyId;
    private Map<Integer, JTextField> questionFields;

    public Survey(int surveyId) {
        this.surveyId = surveyId;
        this.questionFields = new HashMap<>();

        try {
            // Establish JDBC connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost/surveys", "root", "");

            // Set up the UI
            setTitle("Survey Questions");
            setSize(800, 500); // Set a larger size for the frame
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Create a main panel with BorderLayout
            JPanel mainPanel = new JPanel(new BorderLayout());
            add(mainPanel);

            // Panel for holding form components
            formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

            // Scroll pane for the form panel
            JScrollPane scrollPane = new JScrollPane(formPanel);
            mainPanel.add(scrollPane, BorderLayout.CENTER);

            loadQuestions();

            submitButton = new JButton("Submit");
            submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    submitSurvey();
                }
            });

            // Adding the submit button at the end
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(submitButton);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);

            setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadQuestions() {
        try {
            String query = "SELECT id, question_text FROM survey_questions WHERE survey_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, surveyId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int questionId = resultSet.getInt("id");
                String questionText = resultSet.getString("question_text");

                JLabel questionLabel = new JLabel(questionText);
                JTextField answerField = new JTextField();
                answerField.setPreferredSize(new Dimension(300, 25)); // Set preferred width and height

                JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                questionPanel.add(questionLabel);
                questionPanel.add(answerField);

                formPanel.add(questionPanel);
                formPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing

                questionFields.put(questionId, answerField);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void submitSurvey() {
        // Check if any input fields are empty
        for (JTextField textField : questionFields.values()) {
            if (textField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields before submitting.");
                return; // Exit method if any field is empty
            }
        }

        try {
            String insertQuery = "INSERT INTO survey_responses (survey_id, question_id, response_text) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(insertQuery);

            for (Map.Entry<Integer, JTextField> entry : questionFields.entrySet()) {
                int questionId = entry.getKey();
                String responseText = entry.getValue().getText();

                statement.setInt(1, surveyId);
                statement.setInt(2, questionId);
                statement.setString(3, responseText);
                statement.addBatch();
            }

            statement.executeBatch();

            JOptionPane.showMessageDialog(this, "Survey submitted, thank you!");
            dispose();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting survey: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // Assuming surveyId is passed when this class is instantiated.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Survey(1);
            }
        });
    }
}
