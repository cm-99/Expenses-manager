package org.example;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TransactionsManager provides access to applications main data container and asserts that transactions are sorted in descending order.
 * More precisely, it provides read and write access to transactions container
 * and data change notification functionality for classes which implemented TransactionDataChangesListener.
 */
// TODO: Extract data container class with read/write methods. Modify setTransactionsData then to take full control of transactions container.
public class TransactionsManager {
    // Main data container
    private ArrayList<Transaction> transactionsContainer;
    private final ArrayList<TransactionDataChangesListener> transactionDataChangesListeners = new ArrayList<>();

    // TODO: If another way to store data would be considered -
    //  Create interface -? TransactionsStorageManager ?- make this class depend on interface, not implementation
    private final TransactionsCsvFileManager transactionsStorageManager;

    TransactionsManager(File transactionsFile){
        if(transactionsFile == null){
            throw new IllegalArgumentException("Parameter 'transactionsFile' cannot be null.");
        }

        this.transactionsStorageManager = new TransactionsCsvFileManager(transactionsFile);
        try {
            transactionsContainer = transactionsStorageManager.loadStringConvertableObjects();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        sortTransactionsInDescendingOrder();
    }

    private void sortTransactionsInDescendingOrder(){

        // Empty transactionsContainer is a valid state e.g. creating new profile, just return from the method.
        if(transactionsContainer.isEmpty()){
            return;
        }
        // Sort transactions by date - default order is younger -> older transactions.
        transactionsContainer.sort((transaction1, transaction2) ->
        {
            if (transaction1.getDate().isEqual(transaction2.getDate()))
                return 0;
            return transaction1.getDate().isBefore(transaction2.getDate()) ? -1 : 1;
        });
    }

    /* transactionsContainer getters and setters */
    public List<Transaction> getReadOnlyTransactionsDataContainer(){
        if(transactionsContainer == null){
            throw new IllegalStateException("Attributes transactionsContainer must be initialized before calling this method.");
        }
        return Collections.unmodifiableList(transactionsContainer);
    }

    /**
     * Returns the element at the specified position in the transactions container.
     * @param index - index of the element to return
     * @return - the transaction at the specified position in the transactions container.
     */
    public Transaction getTransaction(int index){
        if(index < 0 || index >= transactionsContainer.size()){
            throw new IndexOutOfBoundsException("Parameter 'i' must be a valid transactions container index.");
        }

        return transactionsContainer.get(index);
    }

    /**
     * Adds new transaction to transactionsContainer.
     * @param transaction - transaction to add.
     */
    public void addNewTransaction(Transaction transaction) throws IllegalArgumentException {
        if(transaction == null){
            throw new IllegalArgumentException("Parameter 'transaction' cannot be null");
        }
        int transactionAddedContainerIndex = insertTransactionAtValidIndex(transaction);

        String transactionAsString = transaction.toStringWithState();
        try {
            transactionsStorageManager.addStringConvertableObject(transactionAsString, transactionAddedContainerIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(TransactionDataChangesListener listener : transactionDataChangesListeners){
            listener.updateOnTransactionAdded(transactionAddedContainerIndex);
        }
    }

    /**
     * Replaces transactionToEdit with transactionEdited in transactionsContainer and in the storage managed by transactionsStorageManager.
     * The same effect could be achieved by using add and remove methods in sequence, but it would trigger listeners updates
     * and storage update two times.
     * To clarify - replace means remove one and add the other, but it does not always mean at the same index in container.
     * Transactions descending order must be preserved.
     */
    public void replaceTransaction(Transaction transactionToEdit, Transaction transactionEdited){
        if(transactionEdited == null){
            throw new IllegalArgumentException("Parameter 'transactionEdited' cannot be null");
        }
        if(transactionToEdit == null){
            throw new IllegalArgumentException("Parameter 'transactionToEdit' cannot be null");
        }

        int transactionReplacedOldContainerIndex = transactionsContainer.indexOf(transactionToEdit);
        if(transactionReplacedOldContainerIndex == -1){
            throw new IllegalArgumentException("Parameter 'transactionToEdit' is not an element of transactions container.");
        }
        double transactionReplacedSignedAmountValue = transactionsContainer.get(transactionReplacedOldContainerIndex).getSignedAmount();
        transactionsContainer.remove(transactionReplacedOldContainerIndex);

        int transactionEditedContainerIndex = insertTransactionAtValidIndex(transactionEdited);
        String newTransactionAsString = transactionEdited.toStringWithState();
        transactionsStorageManager.replaceStringConvertableObject(transactionReplacedOldContainerIndex, newTransactionAsString,
                transactionEditedContainerIndex);

        for(TransactionDataChangesListener listener : transactionDataChangesListeners){
            listener.updateOnTransactionReplaced(transactionReplacedOldContainerIndex, transactionEditedContainerIndex,
                    transactionReplacedSignedAmountValue);
        }
    }

    /**
     * Removes transactionToRemove from transactionsContainer and permanent storage managed by transactionsStorageManager
     */
    public void removeTransaction(Transaction transactionToRemove) throws IllegalArgumentException {
        if(transactionToRemove == null) {
            throw new IllegalArgumentException("Parameter 'transactionToRemove' cannot be null");
        }

        int transactionToRemoveIndex = transactionsContainer.indexOf(transactionToRemove);
        double transactionRemovedSingedAmountValue;

        if(transactionToRemoveIndex != -1){
            transactionRemovedSingedAmountValue = transactionToRemove.getSignedAmount();
            transactionsContainer.remove(transactionToRemoveIndex);
        }
        else{
            throw new IllegalArgumentException("Parameter 'transactionToRemove' is not an element of transactions container");
        }

        try {
            transactionsStorageManager.removeStringConvertableObject(transactionToRemoveIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(TransactionDataChangesListener listener : transactionDataChangesListeners){
            listener.updateOnTransactionRemoved(transactionToRemoveIndex, transactionRemovedSingedAmountValue);
        }
    }

    public void addTransactionDataChangesListener(TransactionDataChangesListener transactionDataChangesListener){
        if(transactionDataChangesListener == null){
            throw new IllegalArgumentException("Parameter 'transactionDataChangesListener' cannot be null");
        }

        this.transactionDataChangesListeners.add(transactionDataChangesListener);
    }

    /**
     * Finds valid index for transaction, where valid index is an index that ensures that transactions container is sorted
     * in descending order (younger -> older transactions), adds transaction at this index.
     * @param transaction - transaction to be added at valid index
     * @return - index at which transaction was added
     */
    // This would be slow for adding old transactions in large transactionsContainer - TODO: find better algorithm
    private int insertTransactionAtValidIndex(@NotNull Transaction transaction){
        LocalDate transactionsDate = transaction.getDate();

        // If container is empty, just add it at the beginning
        if(transactionsContainer.size() == 0){
            transactionsContainer.add(transaction);
            return 0;
        }
        else if(transactionsContainer.get(0).getDate().isEqual(transactionsDate)) {
            // If first transaction in the container and new transaction have the same date - add it at the beginning
            transactionsContainer.add(0, transaction);
            return 0;
        }
        else if(transactionsContainer.get(transactionsContainer.size() - 1).getDate().isEqual(transactionsDate)
        || transactionsContainer.get(transactionsContainer.size() - 1).getDate().isAfter(transactionsDate) ) {
            // If first transaction in the container and new transaction do not have the same date (checked before),
            // check the date of the last transaction (as not to iterate over the container for no reason)
            // If new transaction and last transaction have the same date OR new transaction is older - just add new transaction at the end
            transactionsContainer.add(transaction);
            return transactionsContainer.size() - 1;
        }
        else{
            // No more easy options
            // Add transaction at correct index in transactionsContainer to assert that container is sorted in descending order.
            for(int i = 0; i < transactionsContainer.size(); i++){
                if(transactionsContainer.get(i).getDate().isBefore(transactionsDate)){
                    transactionsContainer.add(i, transaction);
                    return i;
                }
            }
        }
        return 0;
    }
}
