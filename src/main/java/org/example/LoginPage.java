package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class LoginPage extends JFrame{

    private static final JPanel panel = new JPanel();

    private static final JLabel usernameLabel = new JLabel("Username");
    private static final JLabel passwordLabel = new JLabel("Password");

    private static final JTextField usernameField = new JTextField();
    private static final JPasswordField passwordField = new JPasswordField();

    private static final JButton loginButton = new JButton("Login");
    private static final JButton createNewProfileButton = new JButton("Create new profile");

    LoginPage(){

        //Set frame parameters
        this.setTitle("Expenses Manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 250);

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/ExpensesManagerIcon.jpg")));
        this.setIconImage(icon.getImage());

        // Set parameters of all the GUI variables
        int frameCenterXpos = this.getWidth() - (this.getWidth() / 2);

        usernameField.setBounds(frameCenterXpos - 95, 30, 190, 30);
        passwordField.setBounds(frameCenterXpos - 95, 80, 190, 30);

        usernameLabel.setBounds(usernameField.getX(), usernameField.getY() - 20, 70, 20);
        passwordLabel.setBounds(passwordField.getX(), passwordField.getY() - 20, 70, 20);

        loginButton.setBounds(frameCenterXpos - 35, this.getHeight() - 110, 70, 25);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(Color.BLACK);
        loginButton.addActionListener((ActionEvent event) -> System.out.println("You pressed button #1"));

        createNewProfileButton.setBounds(frameCenterXpos - 70, this.getHeight() - 80, 140, 25);
        createNewProfileButton.setForeground(Color.WHITE);
        createNewProfileButton.setBackground(Color.BLACK);
        createNewProfileButton.addActionListener((ActionEvent event) -> System.out.println("You pressed button #2"));

        // Might be useful later
        //{JOptionPane.showMessageDialog(this, "Username or password incorrect");});

        panel.setLayout(null);
        panel.add(usernameLabel);
        panel.add(passwordLabel);
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(createNewProfileButton);

        this.add(panel);
        this.setLocationByPlatform(true);
        this.setResizable(false);
        this.setVisible(true);
    }
}
