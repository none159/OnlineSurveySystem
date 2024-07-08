package survey.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminPanel extends JFrame {
    private Connection connection;
    private JTextField surveyTitleField;
    private JTextField surveyDescriptionField;
    private JTextField[] questionFields;
    private JButton addSurveyButton;
    private JButton back = new JButton("back");

    public AdminPanel() {
        try {
            // Establishing JDBC connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost/surveys", "root", "");
            // Set up the UI
            setTitle("Survey Admin Panel");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());
            back.setCursor(new Cursor(Cursor.HAND_CURSOR));
            JPanel surveyPanel = new JPanel(new GridLayout(8, 2, 5, 5));
            surveyPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            surveyPanel.add(new JLabel("Survey Title:"));
            surveyTitleField = new JTextField();
            surveyPanel.add(surveyTitleField);

            surveyPanel.add(new JLabel("Survey Description:"));
            surveyDescriptionField = new JTextField();
            surveyPanel.add(surveyDescriptionField);

            questionFields = new JTextField[5];
            for (int i = 0; i < 5; i++) {
                surveyPanel.add(new JLabel("Question " + (i + 1) + ":"));
                questionFields[i] = new JTextField();
                surveyPanel.add(questionFields[i]);
            }

            addSurveyButton = new JButton("Add Survey");
            surveyPanel.add(addSurveyButton);
            surveyPanel.add(back);
            add(surveyPanel, BorderLayout.CENTER);

            addSurveyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addSurvey();
                }});
            back.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                      dispose();
                      new Menu();
                    }});
          

            setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addSurvey() {
        try {
            // Add survey
            String surveyTitle = surveyTitleField.getText();
            String surveyDescription = surveyDescriptionField.getText();

            if (surveyTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Survey title cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
               return;
            }

            String query = "INSERT INTO survey_master (title, description) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, surveyTitle);
            preparedStatement.setString(2, surveyDescription);
            preparedStatement.executeUpdate();

            // Get generated survey ID
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int surveyId = generatedKeys.getInt(1);

                // Add questions
                query = "INSERT INTO survey_questions (survey_id, question_text) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(query);

                for (JTextField questionField : questionFields) {
                    String questionText = questionField.getText();
                    if (!questionText.isEmpty()) {
                        preparedStatement.setInt(1, surveyId);
                        preparedStatement.setString(2, questionText);
                        preparedStatement.executeUpdate();
                    }
                }

                JOptionPane.showMessageDialog(this, "Survey and questions added successfully!");
                clearFields();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        surveyTitleField.setText("");
        surveyDescriptionField.setText("");
        for (JTextField questionField : questionFields) {
            questionField.setText("");
        }
    }

  
}
