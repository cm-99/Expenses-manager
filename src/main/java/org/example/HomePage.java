package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class HomePage extends JFrame {

    HomePage(){
        // Set JFrame properties
        this.setTitle("Expenses manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setSize(500, 500);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/ExpensesManagerIcon.png")));
        this.setIconImage(icon.getImage());

        // Create an array of items to display in the list
        // TODO: it should be loaded either from local storage or google account when implemented
        String[] testItems = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};

        // Set up scrolling functionality
        JList<String> transactionsList = new JList<>(testItems);
        JScrollPane transactionsScrollPane = new JScrollPane(transactionsList);

        /* Prepare main buttons layout and its components */
        // Create buttons panel and its layout, set their parameters
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(this.getWidth(), 50));
        BoxLayout buttonsLayout = new BoxLayout(buttonsPanel, BoxLayout.X_AXIS);
        buttonsPanel.setLayout(buttonsLayout);

        // Analytics button - should rout the user to analytics page
        JButton analyticsButton = new JButton();

        analyticsButton.setBorder(null);
        analyticsButton.setBorderPainted(false);
        analyticsButton.setMargin(new Insets(0, 0, 0, 0));
        analyticsButton.setContentAreaFilled(false);
        analyticsButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/analytics.png"))));
        analyticsButton.setPressedIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/analyticsPressed.png"))));

        analyticsButton.addActionListener((ActionEvent event) -> System.out.println("You pressed analytics button"));

        // Logout button - should rout the user back to login page
        JButton logoutButton = new JButton();

        logoutButton.setBorder(null);
        logoutButton.setBorderPainted(false);
        logoutButton.setMargin(new Insets(0, 0, 0, 0));
        logoutButton.setContentAreaFilled(false);

        logoutButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/logout.png"))));
        logoutButton.setPressedIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/logoutPressed.png"))));

        logoutButton.addActionListener((ActionEvent event) -> this.dispose());

        // Add components to buttons panel
        buttonsPanel.add(analyticsButton);
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(logoutButton);

        /* Prepare balance panel and its components */
        // Create panel and layout, set their properties
        JPanel balancePanel = new JPanel();
        balancePanel.setPreferredSize(new Dimension(this.getWidth(), 140));
        BoxLayout balanceLayout = new BoxLayout(balancePanel, BoxLayout.Y_AXIS);
        balancePanel.setLayout(balanceLayout);

        // Create labels and set their properties
        JLabel monthSpendingLabel = new JLabel("This month you have spent: ");
        monthSpendingLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // TODO: This should be showing updated value of user spending for current month, to be implemented
        JLabel monthSpendingValueLabel = new JLabel("<VALUE>");
        monthSpendingValueLabel.setFont(new Font("Serif", Font.BOLD, 40));
        monthSpendingValueLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel remainingBudgetLabel = new JLabel("Remaining budget amount: " + "<VALUE>");
        remainingBudgetLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // Add components to balance panel
        balancePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        balancePanel.add(Box.createHorizontalGlue());
        balancePanel.add(monthSpendingLabel);
        balancePanel.add(Box.createRigidArea(new Dimension(this.getWidth(), 10)));
        balancePanel.add(monthSpendingValueLabel);
        balancePanel.add(Box.createRigidArea(new Dimension(this.getWidth(), 30)));
        balancePanel.add(remainingBudgetLabel);

        /* Prepare main layout */
        // Create main panel and its layout, set their parameters
        JPanel mainPanel = new JPanel();
        BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(mainLayout);

        // Add components to main panel
        mainPanel.add(buttonsPanel);
        mainPanel.add(balancePanel);
        mainPanel.add(transactionsScrollPane);

        this.add(mainPanel);
        this.setVisible(true);
    }
}
