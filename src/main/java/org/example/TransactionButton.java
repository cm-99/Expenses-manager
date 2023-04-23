package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

public class TransactionButton extends JButton {

    TransactionButton(Transaction transaction) throws IllegalArgumentException {
        if(transaction == null) {
            throw new IllegalArgumentException("Parameter 'transaction' cannot be null");
        }
        // Set TransactionButton parameters
        this.setForeground(Color.WHITE);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(500, 50));

        // Create main panel and its layout, set their parameters
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        BoxLayout mainLayout = new BoxLayout(mainPanel, BoxLayout.X_AXIS);
        mainPanel.setLayout(mainLayout);

        /* Prepare sub-panels */
        // Prepare icon panel
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        iconPanel.setBackground(Color.white);
        String categoryType = transaction.getTransactionCategory().toString();
        ImageIcon categoryTypeIcon = new ImageIcon(Objects.requireNonNull(getClass().
                getResource("/TransactionCategoryIcons/" +categoryType+ ".png")));
        JLabel iconLabel = new JLabel(categoryTypeIcon);
        iconPanel.add(iconLabel);

        // Prepare labels panel
        JPanel labelsPanel = new JPanel();
        labelsPanel.setBackground(Color.white);
        labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));

        JLabel categoryTypeLabel = new JLabel(categoryType);
        categoryTypeLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        JLabel dateAndDescriptionLabel = new JLabel(transaction.getDate().toString() + " " + transaction.getDescription());

        labelsPanel.add(categoryTypeLabel);
        labelsPanel.add(dateAndDescriptionLabel);
        labelsPanel.setPreferredSize(new Dimension(300, 50));

        // Prepare amount panel
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        amountPanel.setBackground(Color.white);
        Locale locale = Locale.getDefault();
        String currencySymbol = Currency.getInstance(locale).getSymbol();
        JLabel signedAmountLabel = new JLabel(transaction.getSignedAmount() + " " + currencySymbol);
        amountPanel.add(signedAmountLabel);

        // Add components to mainPanel
        mainPanel.add(iconPanel);
        mainPanel.add(labelsPanel);
        mainPanel.add(amountPanel);

        this.add(mainPanel);
        this.setVisible(true);
    }
}
