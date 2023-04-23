package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ApplicationController {
    // Main GUI container(s)
    JFrame mainFrame;
    JTabbedPane tabbedPane;

    // Manager supervising transactions
    TransactionsManager transactionsManager;

    ApplicationController(){
        // Create main JFrame and set properties
        mainFrame = new JFrame("Expenses manager");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationByPlatform(true);
        mainFrame.setSize(500, 500);
        mainFrame.setResizable(false);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/ExpensesManagerIcon.png")));
        mainFrame.setIconImage(icon.getImage());

        tabbedPane = new JTabbedPane();
        mainFrame.add(tabbedPane, BorderLayout.CENTER);
    }

    public void promptForUserCredentials(){
        LoginPage loginPage = new LoginPage(this);
        loginPage.setVisible(true);
    }

    public void requestLocalProfileLoading(File transactionsFile){
        if(transactionsFile == null){
            throw new IllegalArgumentException("Parameter 'transactionFile' cannot be null");
        }

        transactionsManager = new TransactionsManager(transactionsFile);
        prepareApplicationGUI();
    }

    private void prepareApplicationGUI(){
        TransactionDataDependantPanel transactionsOverviewPanel = new ExpensesOverviewPanel(transactionsManager, "Overview");
        TransactionDataDependantPanel expensesAnalyticsPanel = new ExpensesAnalyticsPanel(transactionsManager, "Analytics");

        tabbedPane.addTab(transactionsOverviewPanel.getTitle(), transactionsOverviewPanel);
        tabbedPane.addTab(expensesAnalyticsPanel.getTitle(), expensesAnalyticsPanel);

        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
