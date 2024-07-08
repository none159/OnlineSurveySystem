package survey.com;

import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class Feedbacklist {

    public Feedbacklist() {
        // Create JFrame
        JFrame frame = new JFrame("Feedbacks");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 500);

        // Create a table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Name");
        model.addColumn("Email");
        model.addColumn("Rating");
        model.addColumn("Feedback");
        frame.setLocationRelativeTo(null);

        String sql = "SELECT * FROM feedbacks;";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/surveys", "root", "");
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                String name = result.getString("Name");
                String email = result.getString("Email");
                int rating = result.getInt("Rating");
                String feedback = result.getString("Feedback");

                // Convert rating to stars
                StringBuilder stars = new StringBuilder();
                for (int i = 0; i < rating; i++) {
                    stars.append("\u2605"); // Unicode character for star
                }
                model.addRow(new Object[]{name, email, stars.toString(), feedback});
            }
            result.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        // Create JTable
        JTable table = new JTable(model);
        
        table.setEnabled(false);
        // Set custom cell renderer for the rating column
        table.getColumnModel().getColumn(2).setCellRenderer(new StarCellRenderer());
        table.getColumnModel().getColumn(3).setPreferredWidth(700);
        // Add JScrollPane to the frame
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane);

        // Make the frame visible
        frame.setVisible(true);
    }

    // Custom TableCellRenderer for displaying stars based on rating
    static class StarCellRenderer extends JLabel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            setHorizontalAlignment(SwingConstants.CENTER);
            return this;
        }
    }
}
