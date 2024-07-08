package survey.com;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Menu extends JFrame{
JFrame surveys = new JFrame("Menu");
private JPanel menuPanel =  new JPanel();
private JButton btn1 = new JButton("Rate app");
private JButton btn2 = new JButton("Surveys");
private JButton btn3 = new JButton("Feedbacks");
private JButton btn4 = new JButton("Analytics");
private JButton btn5 = new JButton("About");
private JButton btn6 = new JButton("AdminPanel");
private JLabel label1 = new JLabel("Menu");
public Menu() {
	surveys.setVisible(true);
	  surveys.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 surveys.setResizable(false);
	 surveys.setSize(500,500);
	 btn1.setPreferredSize(new Dimension(90, 50));
	 btn2.setPreferredSize(new Dimension(90, 50));
	 btn4.setPreferredSize(new Dimension(90, 50));
	 btn3.setPreferredSize(new Dimension(90, 50));	
	 btn5.setPreferredSize(new Dimension(90, 50));	
	 btn6.setPreferredSize(new Dimension(90, 50));	
	 label1.setHorizontalAlignment(SwingConstants.CENTER);
	 label1.setForeground(Color.MAGENTA);
	 label1.setFont(new Font(Font.SANS_SERIF,  Font.PLAIN, 20));
	 menuPanel.setLayout(new GridLayout(0, 1, 10, 10));
	 btn1.setCursor(new Cursor(Cursor.HAND_CURSOR));
	 btn3.setCursor(new Cursor(Cursor.HAND_CURSOR));
	 btn2.setCursor(new Cursor(Cursor.HAND_CURSOR));
	 btn4.setCursor(new Cursor(Cursor.HAND_CURSOR));
	 btn5.setCursor(new Cursor(Cursor.HAND_CURSOR));
	 btn6.setCursor(new Cursor(Cursor.HAND_CURSOR));
	  menuPanel.add(label1);
	  menuPanel.add(btn2);
	  menuPanel.add(btn4);
	 menuPanel.add(btn1);
	 menuPanel.add(btn3);
	 menuPanel.add(btn5);
	 menuPanel.add(btn6);
	 surveys.add(menuPanel,BorderLayout.CENTER);
      surveys.setLocationRelativeTo(null); 

      btn1.addActionListener(new ActionListener() {
     	 @Override
     	 public void actionPerformed(ActionEvent e) {
     			surveys.dispose();
     			new Rating();
     		 }
     	 
      });

      btn2.addActionListener(new ActionListener() {
     	 @Override
     	 public void actionPerformed(ActionEvent e) {
     		surveys.dispose();
     			new Surveylist(); 
     		 }
     	 
      });

      btn3.addActionListener(new ActionListener() {
     	 @Override
     	 public void actionPerformed(ActionEvent e) {
     		surveys.dispose();
     			new Feedbacklist(); 
     		 }
     	 
      });
      btn4.addActionListener(new ActionListener() {
      	 @Override
      	 public void actionPerformed(ActionEvent e) {
      		surveys.dispose();
      			new Analytics();
      		 }
      	 
       });
      btn5.addActionListener(new ActionListener() {
       	 @Override
       	 public void actionPerformed(ActionEvent e) {
       		surveys.dispose();
       			new About();
       		 }
       	 
        });
      btn6.addActionListener(new ActionListener() {
    	  	 @Override
    	  	 public void actionPerformed(ActionEvent e) {
    	  		surveys.dispose();
    	  			new Login();
    	  		 }
    	  	 
    	   });

	 
	 }



}
