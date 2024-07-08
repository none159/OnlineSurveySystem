package survey.com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class About extends JFrame {
    JFrame surveys = new JFrame("Menu");
    private JPanel aboutPanel = new JPanel();
    private JLabel about = new JLabel("<html>L'application collecte des données sur les utilisateurs au moyen d'un sondage. Les données sont sécurisées et utilisées à votre avantage.</html>");
    private JButton back = new JButton("back");
    private JLabel label1 = new JLabel("About");

    public About() {
        surveys.setVisible(true);
        surveys.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        surveys.setResizable(false);
        surveys.setSize(500, 500);
        back.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label1.setHorizontalAlignment(SwingConstants.CENTER);
        label1.setForeground(Color.darkGray);
        label1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        about.setOpaque(true);
        about.setBackground(Color.GRAY);
        about.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        about.setBackground(Color.GRAY);
        about.setOpaque(true);
        about.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        aboutPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        aboutPanel.add(label1, gbc);


        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH; // Permet à l'élément de remplir l'espace dans les deux directions
        aboutPanel.add(about, gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.CENTER; // Permet à l'élément de remplir l'espace horizontalement
        gbc.weightx = 1.0; // Définit le poids de l'élément pour remplir l'espace disponible
        aboutPanel.add(back, gbc);

        surveys.add(aboutPanel, BorderLayout.CENTER);
        surveys.setLocationRelativeTo(null);

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                surveys.dispose();
                new Menu();
            }
        });
    }
}
