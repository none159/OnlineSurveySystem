package survey.com;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class Rating extends JFrame {
 JFrame rating = new JFrame("Feedback");
 private JPanel ratingPanel=  new JPanel();
 private JSlider slider1 = new JSlider();
 private JTextField textField1 = new JTextField();
 private JTextField textField2 = new JTextField();
 private JTextArea textArea1 = new JTextArea();
 
 private JButton SUBMITBUTTON = new JButton("Submit");
 private JLabel rate = new JLabel();
 private JLabel label = new JLabel("FEEDBACK FORUM :");
 private JLabel label1 = new JLabel("Name :");
 private JLabel label2 = new JLabel("Email :");
 private JLabel label3 = new JLabel("Feedback :");
 Font  f1  = new Font(Font.SANS_SERIF,  Font.BOLD, 15);
 Font  f2  = new Font(Font.SERIF, Font.BOLD,  20);
 Font  f3  = new Font(Font.SANS_SERIF,  Font.PLAIN, 13);
 Border  lineBorder = BorderFactory.createLineBorder(Color.BLACK);
 private JButton back = new JButton("back");
 public Rating() {
	 rating.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 rating.setContentPane(ratingPanel);
	 textArea1.setLineWrap(true);
     textArea1.setWrapStyleWord(true);
	 rating.pack();
	 label1.setFont(f1);
	 label2.setFont(f1);
	 label3.setFont(f1);
	 rate.setFont(f2);
	 rating.setResizable(false);
	 label.setFont(f1);
	 label.setForeground(Color.MAGENTA);
	 label1.setForeground(Color.DARK_GRAY);
	 label2.setForeground(Color.DARK_GRAY);
	 label3.setForeground(Color.DARK_GRAY);
	 rate.setForeground(Color.ORANGE);
	 textArea1.setBorder(lineBorder);
	 textField1.setBorder(lineBorder);
	 textField2.setBorder(lineBorder);
	 textArea1.setFont(f3);
	 textField1.setFont(f3);
	 textField2.setFont(f3);
	 textArea1.setForeground(Color.darkGray);
	 textField1.setForeground(Color.darkGray);
	 textField2.setForeground(Color.darkGray);
	 SUBMITBUTTON.setCursor(new Cursor(Cursor.HAND_CURSOR));
	 slider1.setCursor(new Cursor(Cursor.HAND_CURSOR));
	 rating.setVisible(true);
	 GridBagLayout layout = new GridBagLayout();
	 ratingPanel.setLayout(layout);
	 rating.setSize(900,600);
	 textField1.setPreferredSize(new Dimension(300, 50));
	 textField2.setPreferredSize(new Dimension(300, 50));
	 textArea1.setPreferredSize(new Dimension(300, 300));
	 SUBMITBUTTON.setPreferredSize(new Dimension(60, 50));
  
	 GridBagConstraints gb = new GridBagConstraints();
	  gb.fill = GridBagConstraints.HORIZONTAL;
	  gb.gridx = 0;
	  gb.gridy = 0;
	ratingPanel.add(label,gb);
     gb.fill = GridBagConstraints.HORIZONTAL;
     gb.gridx = 0;
     gb.gridy = 4;
     ratingPanel.add(label1,gb);
     gb.gridx = 1;
     gb.gridy = 4;
	 ratingPanel.add(textField1,gb);
	 gb.gridx = 2;
     gb.gridy = 4;
     ratingPanel.add(label2,gb);
     gb.gridx = 3;
     gb.gridy = 4;
	 ratingPanel.add(textField2,gb);
     gb.fill = GridBagConstraints.HORIZONTAL;
     gb.gridx = 1;
     gb.gridy = 6;
  
	 ratingPanel.add(slider1,gb);
	 gb.gridx = 3;
     gb.gridy = 6;
	 ratingPanel.add(rate,gb);
	   gb.fill = GridBagConstraints.HORIZONTAL;
	   gb.gridx = 0;
	   gb.gridy = 8;
	   ratingPanel.add(label3,gb);
	   gb.gridx = 1;
	   gb.gridy = 8;
       gb.gridwidth=3;

	 ratingPanel.add(textArea1,gb);
	  gb.fill = GridBagConstraints.HORIZONTAL;
	   gb.gridx = 2;
	   gb.gridy = 9;
	   gb.gridwidth=1;

	 ratingPanel.add(SUBMITBUTTON,gb);
	  gb.fill = GridBagConstraints.HORIZONTAL;
	   gb.gridx = 2;
	   gb.gridy++;
	   gb.gridwidth=1;
      gb.anchor = GridBagConstraints.CENTER;
     ratingPanel.add(back,gb);
     slider1.setMinimum(0);
     slider1.setMaximum(5);
     slider1.setValue(5);
     slider1.setFocusable(false);
     rate.setText(" *".repeat(slider1.getValue()));
     slider1.setPaintTicks(true);
     slider1.setPaintLabels(true);
     rating.setLocationRelativeTo(null);
     slider1.setMajorTickSpacing(1);
     back.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
           rating.dispose();
           new Menu();
         }
     });
     SUBMITBUTTON.addActionListener(new ActionListener() {
    	 @Override
    	 public void actionPerformed(ActionEvent e) {
    		 if(textField1.getText().equals("")|| textField2.getText().equals("")||textArea1.getText().equals("")) {
    			 JOptionPane.showMessageDialog(null, "Please Fill All Fields before Submiting");
    		 }else {
    			 try {
    				 String sql ="INSERT INTO feedbacks"+"(Name,Email,Rating,Feedback)"+"Values(?,?,?,?)";
    				 Class.forName("com.mysql.cj.jdbc.Driver");
    				 Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/surveys","root","");
    				 PreparedStatement statement = connection.prepareStatement(sql);
    				 statement.setString(1,textField1.getText());
    				 statement.setString(2,textField2.getText());
    				 statement.setString(3,String.valueOf(slider1.getValue()));
    				 statement.setString(4,textArea1.getText());
    				 statement.executeUpdate();
    				 JOptionPane.showMessageDialog(null, "RATING SENT,Thanks.");
    				 textField1.setText("");
    				 textField2.setText("");
    				 textArea1.setText("");
    			 }catch(Exception ex) {
    				 JOptionPane.showMessageDialog(null, ex.getMessage());
    			 }
    			 
    		 }
    	 }
     });
     slider1.addMouseListener(new MouseAdapter() {
    	 @Override
    	 public void mouseClicked(MouseEvent e) {
    		 rate.setText(" *".repeat(slider1.getValue()));
    	 }
     });
     int maxCharacters = 100;
     Document doc = textArea1.getDocument();
     ((AbstractDocument) doc).setDocumentFilter(new DocumentFilter() {
         public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
             if (doc.getLength() + str.length() <= maxCharacters) {
                 super.insertString(fb, offs, str, a);
             }
         }

         public void replace(FilterBypass fb, int offset, int length, String str, AttributeSet attrs) throws BadLocationException {
             if (doc.getLength() - length + str.length() <= maxCharacters) {
                 super.replace(fb, offset, length, str, attrs);
             }
         }
     });
     }
 
}
