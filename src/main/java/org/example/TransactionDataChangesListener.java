package org.example;

/**
 * Interface to be implemented by all classes that heavily depend on transactions data and should respond to its changes.
 * This mainly concerns classes representing data models based on transactions data.
 */
// Adding another layer of abstraction ContainerDataChangesListener (?) could be a useful interface for other projects.
public interface TransactionDataChangesListener {
    void updateOnTransactionAdded(int transactionAddedContainerIndex);
    void updateOnTransactionRemoved(int transactionRemovedOldContainerIndex, double transactionRemovedSignedAmountValue);
    void updateOnTransactionReplaced(int transactionReplacedOldContainerIndex, int transactionEditedContainerIndex,
                                     double transactionReplacedSignedAmountValue);
    void updateOnTransactionsContainerChange();
}
