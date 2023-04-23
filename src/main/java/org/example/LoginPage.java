package org.example;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class LoginPage extends JFrame{

    private final ApplicationController applicationController;
    private final JTextField usernameField;
    private final JLabel usernameLabel;
    private final JButton loginButton;

    LoginPage(ApplicationController applicationController){

        if(applicationController == null){
            throw new IllegalArgumentException("Parameter 'applicationController' cannot be null");
        }
        this.applicationController = applicationController;

        // Set frame parameters
        this.setTitle("Expenses Manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 200);

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/ExpensesManagerIcon.png")));
        this.setIconImage(icon.getImage());

        // Create all GUI components and set their parameters
        JPanel panel = new JPanel();

        usernameField = new JTextField();
        usernameField.setBounds(this.getWidth()/2 - 95, 30, 190, 30);
        ((AbstractDocument) usernameField.getDocument()).setDocumentFilter(new FileNameFilter());

        usernameLabel = new JLabel("Local profile name");
        usernameLabel.setBounds(usernameField.getX(), usernameField.getY() - 20, 150, 20);

        loginButton = prepareLoginButton();
        JButton createNewProfileButton = prepareNewProfileButton();

        // Might be useful later
        // {JOptionPane.showMessageDialog(this, "Username or password incorrect");});

        panel.setLayout(null);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(loginButton);
        panel.add(createNewProfileButton);

        this.add(panel);
        this.setLocationByPlatform(true);
        this.setResizable(false);
    }

    private JButton prepareLoginButton() {
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(this.getWidth()/2 - 35, this.getHeight() - 110, 70, 25);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(Color.BLACK);
        loginButton.addActionListener((ActionEvent event) ->
        {
            String directory = System.getProperty("user.dir");
            File file = new File(directory);
            File[] filesInDirectory = file.listFiles();
            if (filesInDirectory == null) {
                throw new AssertionError("No files in current working directory found");
            }

            String profileFileName = usernameField.getText() + ".csv";
            File localProfileFile = null;

            // Check if local profile exists in current working directory, request loading if true
            for(File f : filesInDirectory){
                if(f.getName().equals(profileFileName)){
                    localProfileFile = f;
                    break;
                }
            }

            if(localProfileFile != null){
                applicationController.requestLocalProfileLoading(localProfileFile);
                this.dispose();
            }
            else{
                JOptionPane.showMessageDialog(this, "Profile name incorrect or file is missing");
            }
        });

        return loginButton;
    }

    private JButton prepareNewProfileButton(){
        JButton createNewProfileButton = new JButton("Create new local profile");
        createNewProfileButton.setBounds(this.getWidth()/2 - 90, this.getHeight() - 80, 180, 25);
        createNewProfileButton.setForeground(Color.WHITE);
        createNewProfileButton.setBackground(Color.BLACK);
        createNewProfileButton.addActionListener((ActionListener) ->
        {
            usernameLabel.setText("New local profile name");
            usernameLabel.setBounds(usernameField.getX(), usernameField.getY() - 20, 150, 20);
            createNewProfileButton.setEnabled(false);

            loginButton.setText("Create profile");
            loginButton.setBounds(this.getWidth()/2 - 60, this.getHeight() - 110, 120, 25);

            this.repaint();

            // Remove all button action listeners probably should be included in some subclass of JButton
            TransactionCreator.removeAllButtonActionListeners(loginButton);
            // Login button changes to create profile button
            loginButton.addActionListener((ActionListener2) ->
            {
                String directory = System.getProperty("user.dir");
                File file = new File(directory + "/" + usernameField.getText() + ".csv");

                try {
                    if(file.createNewFile()){
                        applicationController.requestLocalProfileLoading(file);
                        this.dispose();
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Profile already exists");
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            });
        });

        return createNewProfileButton;
    }
}

