package org.example;
import javax.swing.*;

public abstract class TransactionDataDependantPanel extends JPanel implements TransactionDataChangesListener{
    protected TransactionsManager transactionsManager;
    protected String title;

    TransactionDataDependantPanel(TransactionsManager transactionsManager, String title) {
        if(transactionsManager == null){
            throw new IllegalArgumentException("Parameter 'transactionsManager' cannot be null.");
        }

        this.transactionsManager = transactionsManager;
        transactionsManager.addTransactionDataChangesListener(this);

        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }
}
