package org.example;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * ExpensesOverviewPanel is responsible for displaying user transactions and current balance (money spend and money left).
 * Besides it provides access point to TransactionsCreator for transactions modification.
 */
public class ExpensesOverviewPanel extends TransactionDataDependantPanel{

    private final TransactionCreator transactionCreator;

    JPanel transactionsView;
    JScrollPane transactionsScrollPane;

    private double currentMonthsExpenses = 0;
    private final JLabel currentMonthsExpensesLabel;

    private double currentMonthsBudgetRemaining = 0;
    private final JLabel currentMonthsBudgetRemainingLabel;

    ExpensesOverviewPanel(TransactionsManager transactionsManager, String title) {
        super(transactionsManager, title);
        this.transactionCreator = new TransactionCreator(transactionsManager);
        this.setSize(500, 400);

        /* Set up transactions view with scrolling functionality */
        transactionsView = new JPanel();
        transactionsView.setBackground(Color.white);
        BoxLayout transactionViewLayout = new BoxLayout(transactionsView, BoxLayout.Y_AXIS);
        transactionsView.setLayout(transactionViewLayout);
        transactionsView.setPreferredSize(new Dimension(this.getWidth(), 1000));

        transactionsScrollPane = new JScrollPane(transactionsView);
        transactionsScrollPane.setPreferredSize(new Dimension(this.getWidth(), 400));
        transactionsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        updateOnTransactionsContainerChange();
        transactionsView.add(new Box.Filler(new Dimension(0, 0),
                new Dimension(0, Short.MAX_VALUE),
                new Dimension(0, Short.MAX_VALUE)));

        /* Prepare balance panel and its components */
        // Create panel and layout, set their properties
        JPanel balancePanel = new JPanel();
        balancePanel.setBackground(Color.white);
        balancePanel.setPreferredSize(new Dimension(this.getWidth(), 140));
        BoxLayout balanceLayout = new BoxLayout(balancePanel, BoxLayout.Y_AXIS);
        balancePanel.setLayout(balanceLayout);

        // Create labels and set their properties
        // TODO: Add currency as transaction attribute. Default locale currency should be used in TransactionCreator as... default currency choice.
        JLabel monthSpendingLabel = new JLabel("This month you have spent: ");
        monthSpendingLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        currentMonthsExpensesLabel = new JLabel();
        currentMonthsExpensesLabel.setFont(new Font("Serif", Font.BOLD, 40));
        currentMonthsExpensesLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        currentMonthsBudgetRemainingLabel = new JLabel();
        currentMonthsBudgetRemainingLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        updateBalanceLabels();

        // Add components to balance panel
        balancePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        balancePanel.add(Box.createHorizontalGlue());
        balancePanel.add(monthSpendingLabel);
        balancePanel.add(Box.createRigidArea(new Dimension(this.getWidth(), 10)));
        balancePanel.add(currentMonthsExpensesLabel);
        balancePanel.add(Box.createRigidArea(new Dimension(this.getWidth(), 30)));
        balancePanel.add(currentMonthsBudgetRemainingLabel);

        // Prepare button for adding transactions
        JPanel addTransactionPanel = new JPanel();
        addTransactionPanel.setBackground(Color.white);
        addTransactionPanel.setLayout(new BoxLayout(addTransactionPanel, BoxLayout.X_AXIS));
        JButton addTransactionButton = new JButton();
        addTransactionPanel.add(addTransactionButton);

        addTransactionButton.setBorder(null);
        addTransactionButton.setBorderPainted(false);
        addTransactionButton.setMargin(new Insets(0, 0, 0, 0));
        addTransactionButton.setContentAreaFilled(false);

        addTransactionButton.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/HomePageIcons/addTransaction.png"))));
        addTransactionButton.setPressedIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/HomePageIcons/addTransactionPressed.png"))));

        addTransactionButton.addActionListener((ActionEvent event) -> transactionCreator.openInNewTransactionMode());

        /* Prepare main layout */
        // Create main panel and its layout, set their parameters
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(mainLayout);

        // Add components to main panel
        mainPanel.add(balancePanel);
        mainPanel.add(transactionsScrollPane);
        mainPanel.add(addTransactionPanel);

        this.add(mainPanel);
        this.setVisible(true);
    }

    @Override
    public void updateOnTransactionAdded(int transactionAddedContainerIndex) {
        Transaction transaction = transactionsManager.getTransaction(transactionAddedContainerIndex);

        TransactionButton newTransactionButton = new TransactionButton(transaction);
        newTransactionButton.addActionListener((ActionEvent event) -> transactionCreator.openInTransactionEditMode(transaction));
        transactionsView.add(newTransactionButton, transactionAddedContainerIndex);

        transactionsView.repaint();
        transactionsView.revalidate();

        updateBalanceWithNewTransaction(transaction);
        updateBalanceLabels();
    }

    @Override
    public void updateOnTransactionRemoved(int transactionRemovedOldContainerIndex, double transactionRemovedSignedAmountValue) {
        transactionsView.remove(transactionRemovedOldContainerIndex);

        transactionsView.repaint();
        transactionsView.revalidate();

        if(transactionRemovedSignedAmountValue < 0) {
            currentMonthsExpenses = currentMonthsExpenses - transactionRemovedSignedAmountValue;
        }
        currentMonthsBudgetRemaining = currentMonthsBudgetRemaining - transactionRemovedSignedAmountValue;
        updateBalanceLabels();
    }

    @Override
    public void updateOnTransactionReplaced(int transactionReplacedOldContainerIndex, int transactionEditedContainerIndex, double transactionReplacedSignedAmountValue) {

        // Update buttons
        Transaction transactionEdited = transactionsManager.getTransaction(transactionEditedContainerIndex);
        TransactionButton transactionButtonEdited = new TransactionButton(transactionEdited);
        transactionButtonEdited.addActionListener((ActionEvent event) -> transactionCreator.openInTransactionEditMode(transactionEdited));

        transactionsView.remove(transactionReplacedOldContainerIndex);
        transactionsView.add(transactionButtonEdited, transactionEditedContainerIndex);

        transactionsView.repaint();
        transactionsView.revalidate();

        // Update balance
        if(transactionReplacedSignedAmountValue < 0) {
            currentMonthsExpenses = currentMonthsExpenses - transactionReplacedSignedAmountValue;
        }
        currentMonthsBudgetRemaining = currentMonthsBudgetRemaining - transactionReplacedSignedAmountValue ;
        updateBalanceWithNewTransaction(transactionsManager.getTransaction(transactionEditedContainerIndex));
        updateBalanceLabels();
    }

    /**
     * Creates buttons corresponding to transactions in new transactions container and displays them in transactionsView,
     * calculates currentMonthsExpenses and currentMonthsBudgetRemaining and displays it.
     */
    @Override
    public void updateOnTransactionsContainerChange() {
        Component[] transactionsViewComponents = transactionsView.getComponents();
        if(transactionsViewComponents.length != 0){
            transactionsView.removeAll();
        }

        List<Transaction> transactionsList = transactionsManager.getReadOnlyTransactionsDataContainer();
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfCurrentMonth = today.withDayOfMonth(1);

        for (Transaction transaction : transactionsList) {
            TransactionButton transactionButton = new TransactionButton(transaction);
            transactionButton.addActionListener((ActionEvent event) -> transactionCreator.openInTransactionEditMode(transaction));
            transactionsView.add(transactionButton);

            boolean transactionIsFromCurrentMonth =
                    firstDayOfCurrentMonth.isBefore(transaction.getDate()) || firstDayOfCurrentMonth.isEqual(transaction.getDate());

            if(transactionIsFromCurrentMonth){
                updateBalanceWithNewTransaction(transaction);
            }
        }
    }

    private void updateBalanceWithNewTransaction(@NotNull Transaction transaction) {
        double signedAmount = transaction.getSignedAmount();
        if(signedAmount < 0) {
            currentMonthsExpenses += signedAmount;
        }
        currentMonthsBudgetRemaining += signedAmount;
    }

    private void updateBalanceLabels() {
        String localCurrencySymbol = Currency.getInstance(Locale.getDefault()).getSymbol();

        currentMonthsExpensesLabel.setText(Math.abs(currentMonthsExpenses) + " " + localCurrencySymbol);
        currentMonthsBudgetRemainingLabel.setText("Remaining budget amount: " + currentMonthsBudgetRemaining + " " + localCurrencySymbol);
        if(currentMonthsBudgetRemaining < 0){
            currentMonthsBudgetRemainingLabel.setForeground(Color.RED);
        }
        else{
            currentMonthsBudgetRemainingLabel.setForeground(Color.BLACK);
        }

        currentMonthsExpensesLabel.repaint();
        currentMonthsBudgetRemainingLabel.repaint();
    }
}


