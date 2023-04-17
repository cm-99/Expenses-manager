package org.example;

import java.time.LocalDate;

enum TransactionType{
    INCOME("income"),
    EXPENSE("expense");

    private String name;
    TransactionType(String name){this.name = name;};

    @Override
    public String toString(){
        return name;
    }
}
enum TransactionCategory{
    BILLS("Bills"),
    CHILDREN("Children"),
    CLOTHING("Clothing"),
    EDUCATION("Education"),
    EXTERNAL_INCOME("External income"),
    FOOD("Food"),
    FOR_OTHERS("For others"),
    HEALTH("Health"),
    HOME("Home"),
    INTERNAL_INCOME("Internal income"),
    LEISURE_AND_RECREATION("Leisure and recreation"),
    TAXES_AND_CHARGES("Taxes and charges"),
    TRANSPORT("Transport"),
    UNCATEGORIZED("Uncategorized");

    private String name;
    TransactionCategory(String name){this.name = name;};

    @Override
    public String toString(){
        return name;
    }
}

public class Transaction {

    private double amount = 0;
    private String description = "";
    private LocalDate date;
    private TransactionType transactionType;
    private TransactionCategory transactionCategory;

    // Default empty constructor
    Transaction(){}

    // Full constructor just in case
    Transaction(double amount, String description, LocalDate date, TransactionType transactionType, TransactionCategory transactionCategory){
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
    }

    public double getAmount() {
        return amount;
    }

    public double getSignedAmount() {
        if(this.transactionType == TransactionType.INCOME)
        {
            return amount;
        }
        else
        {
            return -amount;
        }
    }

    public void setAmount(int amount) {
        if(amount >= 0) {
            this.amount = amount;
        }
        else
        {
            throw new IllegalArgumentException("Transaction.amount can't have negative value");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if(description.length() <= 20) {
            this.description = description;
        }
        else{
            throw new IllegalArgumentException("Transaction.description may have a maximum of 20 characters");
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionCategory getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(TransactionCategory transactionCategory) {
        this.transactionCategory = transactionCategory;
    }
}
