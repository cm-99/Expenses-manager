package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

public class HomePage extends JFrame {

    private ArrayList<Transaction> transactionsList = new ArrayList<>();
    private double totalMonthsExpenses = 0;
    private double monthsBudgetRemaining = 0;

    HomePage(){
        // Set JFrame properties
        this.setTitle("Expenses manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setSize(500, 500);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/ExpensesManagerIcon.png")));
        this.setIconImage(icon.getImage());

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
        Locale locale = Locale.getDefault();
        String currencySymbol = Currency.getInstance(locale).getSymbol();

        JLabel totalMonthsExpensesLabel = new JLabel(totalMonthsExpenses + " " + currencySymbol);
        totalMonthsExpensesLabel.setFont(new Font("Serif", Font.BOLD, 40));
        totalMonthsExpensesLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel remainingBudgetLabel = new JLabel("Remaining budget amount: " + monthsBudgetRemaining + " " + currencySymbol);
        remainingBudgetLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        // Add components to balance panel
        balancePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        balancePanel.add(Box.createHorizontalGlue());
        balancePanel.add(monthSpendingLabel);
        balancePanel.add(Box.createRigidArea(new Dimension(this.getWidth(), 10)));
        balancePanel.add(totalMonthsExpensesLabel);
        balancePanel.add(Box.createRigidArea(new Dimension(this.getWidth(), 30)));
        balancePanel.add(remainingBudgetLabel);

        // Load user transactions
        // TODO: it should be loaded either from local storage or google account when implemented
        Transaction exampleTransaction = new Transaction();
        exampleTransaction.setAmount(10);
        exampleTransaction.setDescription("Test");
        exampleTransaction.setDate(LocalDate.of(2023, 4, 16));
        exampleTransaction.setTransactionType(TransactionType.INCOME);
        exampleTransaction.setTransactionCategory(TransactionCategory.HOME);
        transactionsList.add(exampleTransaction);

        /* Set up transactions view with scrolling functionality */
        JPanel transactionsView = new JPanel();
        BoxLayout transactionViewLayout = new BoxLayout(transactionsView, BoxLayout.Y_AXIS);
        transactionsView.setLayout(transactionViewLayout);
        transactionsView.setPreferredSize(new Dimension(this.getWidth(), 300));
        JScrollPane transactionsScrollPane = new JScrollPane(transactionsView);
        transactionsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Create transaction buttons for all transactions in users data, calculate months expenses and budget left
        if(!transactionsList.isEmpty()){
            for (Transaction transaction : transactionsList) {
                TransactionButton transactionButton = new TransactionButton(transaction);
                transactionsView.add(transactionButton);

                double signedAmount = transaction.getSignedAmount();
                if(signedAmount < 0) {
                    totalMonthsExpenses += signedAmount;
                }
                else{
                    monthsBudgetRemaining += signedAmount;
                }
            }
        }
        else{
            JLabel emptyViewLabel = new JLabel("No transactions found");
            emptyViewLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
            transactionsView.add(emptyViewLabel);
        }

        monthsBudgetRemaining = monthsBudgetRemaining - totalMonthsExpenses;
        monthsBudgetRemaining = monthsBudgetRemaining < 0 ? 0 : monthsBudgetRemaining;

        transactionsView.add(new Box.Filler(new Dimension(0, 0),
                new Dimension(0, Short.MAX_VALUE),
                new Dimension(0, Short.MAX_VALUE)));

        // Prepare button for adding transactions
        JPanel addTransactionPanel = new JPanel();
        addTransactionPanel.setLayout(new BoxLayout(addTransactionPanel, BoxLayout.X_AXIS));
        JButton addTransactionButton = new JButton();
        addTransactionPanel.add(addTransactionButton);

        addTransactionButton.setBorder(null);
        addTransactionButton.setBorderPainted(false);
        addTransactionButton.setMargin(new Insets(0, 0, 0, 0));
        addTransactionButton.setContentAreaFilled(false);

        addTransactionButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/addTransaction.png"))));
        addTransactionButton.setPressedIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/addTransactionPressed.png"))));

        addTransactionButton.addActionListener((ActionEvent event) -> System.out.println("You pressed add transaction button"));

        /* Prepare main layout */
        // Create main panel and its layout, set their parameters
        JPanel mainPanel = new JPanel();
        BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(mainLayout);

        // Add components to main panel
        mainPanel.add(buttonsPanel);
        mainPanel.add(balancePanel);
        mainPanel.add(transactionsScrollPane);
        mainPanel.add(addTransactionPanel);

        this.add(mainPanel);
        this.setVisible(true);
    }
}
