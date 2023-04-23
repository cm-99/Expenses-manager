package org.example;

public class ExpensesAnalyticsPanel extends TransactionDataDependantPanel {

    ExpensesAnalyticsPanel(TransactionsManager transactionsManager, String title){
        super(transactionsManager, title);
    }

    @Override
    public void updateOnTransactionAdded(int transactionAddedContainerIndex) {

    }

    @Override
    public void updateOnTransactionRemoved(int transactionRemovedOldContainerIndex, double transactionRemovedSignedAmountValue) {

    }

    @Override
    public void updateOnTransactionReplaced(int transactionReplacedOldContainerIndex, int transactionEditedContainerIndex, double transactionReplacedSignedAmountValue) {

    }

    @Override
    public void updateOnTransactionsContainerChange() {

    }
}
