package survey.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class AdminPanel extends JFrame {
    private Connection connection;
    private JTextField surveyTitleField;
    private JTextField surveyDescriptionField;
    private JTextField[] questionFields;
    private JButton addSurveyButton;
    private JButton editSurveyButton;
    private JButton removeSurveyButton;
    private JButton backButton;

    public AdminPanel() {
        try {
            // Establishing JDBC connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost/surveys", "root", "");
            // Set up the UI
            setTitle("Survey Admin Panel");
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            JPanel surveyPanel = new JPanel(new GridLayout(9, 2, 5, 5));
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
            editSurveyButton = new JButton("Edit Survey");
            removeSurveyButton = new JButton("Remove Survey");
            backButton = new JButton("Back");

            surveyPanel.add(addSurveyButton);
            surveyPanel.add(editSurveyButton);
            surveyPanel.add(removeSurveyButton);
            surveyPanel.add(backButton);

            add(surveyPanel, BorderLayout.CENTER);

            addSurveyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addSurvey();
                }
            });
            editSurveyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    editSurvey();
                }
            });
            removeSurveyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeSurvey();
                }
            });
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new Menu();
                }
            });

            setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addSurvey() {
        try {
            // Get input values
            String surveyTitle = surveyTitleField.getText().trim();
            String surveyDescription = surveyDescriptionField.getText().trim();

            // Validate input
            if (surveyTitle.isEmpty() || surveyDescription.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill out all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean allQuestionsFilled = true;
            for (JTextField questionField : questionFields) {
                String questionText = questionField.getText().trim();
                if (questionText.isEmpty()) {
                    allQuestionsFilled = false;
                    break;
                }
            }

            if (!allQuestionsFilled) {
                JOptionPane.showMessageDialog(this, "Please fill out all question fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Prepare insert statements
            String insertSurveyQuery = "INSERT INTO survey_master (title, description) VALUES (?, ?)";
            String insertQuestionQuery = "INSERT INTO survey_questions (survey_id, question_text) VALUES (?, ?)";

            PreparedStatement insertSurveyStatement = connection.prepareStatement(insertSurveyQuery, Statement.RETURN_GENERATED_KEYS);
            insertSurveyStatement.setString(1, surveyTitle);
            insertSurveyStatement.setString(2, surveyDescription);

            // Execute insert survey query
            int affectedRows = insertSurveyStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating survey failed, no rows affected.");
            }

            // Retrieve generated survey ID
            ResultSet generatedKeys = insertSurveyStatement.getGeneratedKeys();
            int surveyId;
            if (generatedKeys.next()) {
                surveyId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating survey failed, no ID obtained.");
            }

            // Insert questions
            PreparedStatement insertQuestionStatement = connection.prepareStatement(insertQuestionQuery);
            for (JTextField questionField : questionFields) {
                String questionText = questionField.getText().trim();
                if (!questionText.isEmpty()) {
                    insertQuestionStatement.setInt(1, surveyId);
                    insertQuestionStatement.setString(2, questionText);
                    insertQuestionStatement.addBatch();
                }
            }

            // Execute batch insert for questions
            insertQuestionStatement.executeBatch();

            JOptionPane.showMessageDialog(this, "Survey and questions added successfully!");
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding survey: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSurvey() {
        try {
            String surveyTitle = surveyTitleField.getText().trim();
            String surveyDescription = surveyDescriptionField.getText().trim();

            if (surveyTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Survey title cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get survey ID
            String query = "SELECT id FROM survey_master WHERE title = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, surveyTitle);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int surveyId = resultSet.getInt("id");

                // Update survey description
                query = "UPDATE survey_master SET description = ? WHERE id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, surveyDescription);
                preparedStatement.setInt(2, surveyId);
                preparedStatement.executeUpdate();

                // Get current questions
                query = "SELECT id, question_text FROM survey_questions WHERE survey_id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, surveyId);
                ResultSet questionResultSet = preparedStatement.executeQuery();

                Map<Integer, String> currentQuestions = new HashMap<>();
                while (questionResultSet.next()) {
                    currentQuestions.put(questionResultSet.getInt("id"), questionResultSet.getString("question_text"));
                }

                // Prompt for question ID to edit
                String questionIdStr = JOptionPane.showInputDialog(this, "Enter Question ID to edit:");
                if (questionIdStr != null && !questionIdStr.trim().isEmpty()) {
                    int questionId = Integer.parseInt(questionIdStr.trim());

                    if (currentQuestions.containsKey(questionId)) {
                        String newQuestionText = JOptionPane.showInputDialog(this, "Enter new question text:");
                        if (newQuestionText != null && !newQuestionText.isEmpty()) {
                            // Update the selected question
                            query = "UPDATE survey_questions SET question_text = ? WHERE id = ?";
                            preparedStatement = connection.prepareStatement(query);
                            preparedStatement.setString(1, newQuestionText);
                            preparedStatement.setInt(2, questionId);
                            preparedStatement.executeUpdate();

                            JOptionPane.showMessageDialog(this, "Question updated successfully!");
                        } else {
                            JOptionPane.showMessageDialog(this, "Question text cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid Question ID!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Question ID!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Survey not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error editing survey: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Question ID format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeSurvey() {
        try {
            String surveyTitle = surveyTitleField.getText().trim();

            if (surveyTitle.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Survey title cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Start transaction
            connection.setAutoCommit(false);

            // Get survey ID
            String query = "SELECT id FROM survey_master WHERE title = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, surveyTitle);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int surveyId = resultSet.getInt("id");

                // Delete survey questions
                query = "DELETE FROM survey_questions WHERE survey_id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, surveyId);
                preparedStatement.executeUpdate();

                // Delete survey
                query = "DELETE FROM survey_master WHERE id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, surveyId);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(this, "Survey removed successfully!");
                clearFields();

                // Commit transaction
                connection.commit();
            } else {
                JOptionPane.showMessageDialog(this, "Survey not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Restore auto-commit mode
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Rollback transaction on error
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }
    }

    private void clearFields() {
        surveyTitleField.setText("");
        surveyDescriptionField.setText("");
        for (JTextField questionField : questionFields) {
            questionField.setText("");
        }
    }

    public static void main(String[] args) {
        new AdminPanel();
    }
}
