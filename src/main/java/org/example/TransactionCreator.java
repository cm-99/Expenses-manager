package org.example;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Locale;

/**
 * TransactionCreator class provides ability to create new and edit existing transactions through JDialog.
 */
public class TransactionCreator extends JDialog {
    private final TransactionsManager transactionsManager;

    // Input components
    JFormattedTextField amountField;
    JTextField descriptionField;
    JSpinner dateSpinner;
    JComboBox<TransactionType> transactionTypesComboBox;
    JComboBox<TransactionCategory> transactionCategoryComboBox;

    // Buttons
    JButton updateButton;
    JButton deleteButton;

    /**
     * Default TransactionCreator constructor.
     * @param transactionsManager - "parent" class which is a receiver of created/edited transactions
     */
    TransactionCreator(TransactionsManager transactionsManager) throws IllegalArgumentException {

        if(transactionsManager == null)
        {
            throw new IllegalArgumentException("Parameter 'homePage' cannot be null.");
        }

        this.transactionsManager = transactionsManager;

        // Set JDialog parameters
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setResizable(false);
        this.setLocationByPlatform(true);

        // Prepare main panel and sub-panels
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel inputComponentsPanel = new JPanel();
        GridLayout inputComponentsPanelLayout = new GridLayout(5, 2);
        inputComponentsPanelLayout.setVgap(10);
        inputComponentsPanel.setLayout(inputComponentsPanelLayout);
        inputComponentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        /* Prepare input components with their labels */
        // Amount input
        JLabel amountLabel = new JLabel("Amount ");
        DecimalFormat doubleFormat = new DecimalFormat("#0.00");
        NumberFormatter doubleFormatter = new NumberFormatter(doubleFormat);
        doubleFormatter.setValueClass(Double.class);
        doubleFormatter.setAllowsInvalid(false);
        amountField = new JFormattedTextField(doubleFormatter);
        amountField.setValue(0);

        // Description input
        JLabel descriptionLabel = new JLabel("Description ");
        descriptionField = new JTextField(Transaction.getMaxDescriptionLength());
        DocumentFilter descriptionFieldFilter = new DocumentLengthFilter(Transaction.getMaxDescriptionLength());
        ((AbstractDocument)descriptionField.getDocument()).setDocumentFilter(descriptionFieldFilter);
        descriptionField.setText("");

        // Date input
        JLabel dateLabel = new JLabel("Date ");
        LocalDate today = LocalDate.now();
        Date dateToday = Date.valueOf(today);
        dateSpinner = new JSpinner(new SpinnerDateModel(dateToday, null, null, Calendar.DATE));
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(editor);

        // Transaction type input
        JLabel transactionTypeLabel = new JLabel("Type ");
        transactionTypesComboBox = new JComboBox<>(TransactionType.values());

        // Transaction category input
        JLabel transactionCategoryLabel = new JLabel("Category ");
        transactionCategoryComboBox = new JComboBox<>(TransactionCategory.values());

        /* Prepare buttons */
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener((ActionListener) -> this.setVisible(false));

        // Lay out all input components
        inputComponentsPanel.add(amountLabel);
        inputComponentsPanel.add(amountField);
        inputComponentsPanel.add(descriptionLabel);
        inputComponentsPanel.add(descriptionField);
        inputComponentsPanel.add(dateLabel);
        inputComponentsPanel.add(dateSpinner);
        inputComponentsPanel.add(transactionTypeLabel);
        inputComponentsPanel.add(transactionTypesComboBox);
        inputComponentsPanel.add(transactionCategoryLabel);
        inputComponentsPanel.add(transactionCategoryComboBox);

        // Lay out buttons
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(updateButton);
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(Box.createHorizontalGlue());

        mainPanel.add(inputComponentsPanel);
        mainPanel.add(buttonsPanel);

        this.add(mainPanel);
        this.pack();
    }

    /**
     * Parses values from input components to create new Transaction object.
     * @return - created Transaction object.
     */
    Transaction createTransactionFromUserInput(){

        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        double amount = 0;
        try {
            Number number = format.parse(amountField.getText());
            amount = number.doubleValue();
        } catch(java.text.ParseException e) {
            e.printStackTrace();
        }

        java.util.Date spinnerDate = (java.util.Date) dateSpinner.getValue();
        LocalDate localDate = spinnerDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return new Transaction(Math.abs(amount), descriptionField.getText(), localDate,
                (TransactionType)transactionTypesComboBox.getSelectedItem(),
                (TransactionCategory)transactionCategoryComboBox.getSelectedItem());
    }

    /**
     * Opens TransactionCreator with input components set to values of corresponding transaction attributes.
     * Depending on user's choice - relays new transaction to transactionDataController on successful edit or requests transaction deletion
     *
     * @param transactionToEdit - transaction which PROVIDES DATA TO EDIT.
     *                          Object pointed to by this parameter is never modified,
     *                          a new one is created with data modified by the user.
     */
    void openInTransactionEditMode(Transaction transactionToEdit) throws IllegalArgumentException, IllegalStateException {

        if(transactionToEdit == null) {
            throw new IllegalArgumentException("Parameter 'transactionToEdit' cannot be null");
        }
        
        // Remove previous action listeners
        removeAllButtonActionListeners(updateButton);
        removeAllButtonActionListeners(deleteButton);

        amountField.setValue(transactionToEdit.getAmount());
        descriptionField.setText(transactionToEdit.getDescription());
        dateSpinner.setValue(Date.valueOf(transactionToEdit.getDate()));
        transactionTypesComboBox.setSelectedItem(transactionToEdit.getTransactionType());
        transactionCategoryComboBox.setSelectedItem(transactionToEdit.getTransactionCategory());

        updateButton.setText("Update");
        updateButton.addActionListener((ActionListener) -> {
            transactionsManager.replaceTransaction(transactionToEdit, createTransactionFromUserInput());
            this.setVisible(false);
        });

        deleteButton.addActionListener((ActionListener) -> {
            transactionsManager.removeTransaction(transactionToEdit);
            this.setVisible(false);
        });

        deleteButton.setEnabled(true);
        this.setVisible(true);
    }

    /**
     * Opens TransactionCreator with input components set to default values of corresponding transaction attributes,
     * relays created transaction to transactionDataController on successful creation.
     */
    void openInNewTransactionMode(){

        // Remove previous action listeners
        removeAllButtonActionListeners(updateButton);
        removeAllButtonActionListeners(deleteButton);

        LocalDate today = LocalDate.now();
        Date dateToday = Date.valueOf(today);

        amountField.setValue(0);
        descriptionField.setText("");
        dateSpinner.setValue(dateToday);
        transactionTypesComboBox.setSelectedItem(TransactionType.EXPENSE);
        transactionCategoryComboBox.setSelectedItem(TransactionCategory.BILLS);

        updateButton.setText("Add");
        updateButton.addActionListener((ActionListener) -> {
            Transaction transaction = createTransactionFromUserInput();
            transactionsManager.addNewTransaction(transaction);
            this.setVisible(false);
        });

        deleteButton.setEnabled(false);
        this.setVisible(true);
    }

    /**
     * Removes all action listeners from the button parameter.
     */
    public static void removeAllButtonActionListeners(JButton button) throws IllegalArgumentException{
        if(button == null) {
            throw new IllegalArgumentException("Parameter 'button' cannot be null");
        }

        ActionListener[] buttonActionListeners = button.getActionListeners();
        for(ActionListener al : buttonActionListeners){
            button.removeActionListener(al);
        }
    }
}

