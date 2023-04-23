package org.example;

import org.jetbrains.annotations.NotNull;
import org.junit.platform.commons.util.Preconditions;
import java.time.LocalDate;


/**
 * Main type of transaction.
 */
enum TransactionType{
    INCOME("income"),
    EXPENSE("expense");

    private final String name;
    TransactionType(String name){this.name = name;}

    @Override
    public String toString(){
        return name;
    }

    public static TransactionType fromString(String text) {
        if(text == null){throw new IllegalArgumentException("Parameter 'text' cannot be null");}
        for (TransactionType type : TransactionType.values()) {
            if (type.name.equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}

/**
 * Transaction categories - might be extended.
 */
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

    private final String name;
    TransactionCategory(String name){this.name = name;}

    @Override
    public String toString(){
        return name;
    }

    public static TransactionCategory fromString(String text) {
        if(text == null){throw new IllegalArgumentException("Parameter 'text' cannot be null");}
        for (TransactionCategory category : TransactionCategory.values()) {
            if (category.name.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}

/**
 * Transaction class holds individual transaction's data and provides access point to it.
 */
public class Transaction implements StringConvertableObject{

    private double amount;
    private String description;
    private LocalDate date;
    private TransactionType transactionType;
    private TransactionCategory transactionCategory;

    private static final int maxDescriptionLength = 20;

    // Default full constructor
    Transaction(double amount, @NotNull String description, LocalDate date, TransactionType transactionType, TransactionCategory transactionCategory) {
        Preconditions.notNull(date, "Transaction date cannot be null");
        Preconditions.notNull(transactionType, "Transaction type cannot be null");
        Preconditions.notNull(transactionCategory, "Transaction category cannot be null");

        this.amount = Math.abs(amount);
        if(description.length() > maxDescriptionLength){
            description = description.substring(0, maxDescriptionLength - 1);
        }
        this.description = description;
        this.date = date;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
    }

    /**
     * Constructor utilizing StringConvertableObject interface to set Transaction attributes from string.
     * @param objectAsStringWithState - string which should have been previously obtained from corresponding toStringWithState method.
     */
    Transaction(String objectAsStringWithState){
        this.setStateFromString(objectAsStringWithState);
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

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public TransactionCategory getTransactionCategory() {
        return transactionCategory;
    }

    public static int getMaxDescriptionLength(){
        return maxDescriptionLength;
    }

    @Override
    public String toStringWithState() {
        char delimiter = ';';
        Preconditions.notNull(this.date, "Transaction date cannot be null");
        Preconditions.notNull(this.transactionType, "Transaction type cannot be null");
        Preconditions.notNull(this.transactionCategory, "Transaction category type cannot be null");

        return Double.toString(this.amount) + delimiter +
                description + delimiter +
                this.date.toString() + delimiter +
                this.transactionType.toString() + delimiter +
                this.transactionCategory.toString();
    }

    @Override
    public void setStateFromString(String objectAsStringWithState) {
        if(objectAsStringWithState == null){
            throw new IllegalArgumentException("Parameter 'objectAsStringWithState' cannot be null");
        }

        char delimiter = ';';
        String[] attributesStringsList = objectAsStringWithState.split(String.valueOf(delimiter));
        int transactionAttributesNumber = 5;
        if(attributesStringsList.length != transactionAttributesNumber){
            throw new IllegalArgumentException("Parameter 'objectAsStringWithState' does not have a proper number of" +
                    "attributes or incorrect delimiter was used");
        }

        this.amount = Double.parseDouble(attributesStringsList[0]);
        this.description = attributesStringsList[1];
        this.date = LocalDate.parse(attributesStringsList[2]);
        this.transactionType = TransactionType.fromString(attributesStringsList[3]);
        this.transactionCategory = TransactionCategory.fromString(attributesStringsList[4]);
    }
}
