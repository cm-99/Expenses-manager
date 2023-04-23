package org.example;


import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    /* signedAmount() tests */
    @Test
    void transactionConstructedWithNegativeAmountAndTypeIncomeShouldReturnPositiveSignedAmount() {
        Transaction transaction = new Transaction(-10, "", LocalDate.now(), TransactionType.INCOME, TransactionCategory.BILLS);
        assertTrue(transaction.getSignedAmount() > 0);
    }

    @Test
    void transactionConstructedWithPositiveAmountAndTypeExpenseShouldReturnNegativeSignedAmount() {
        Transaction transaction = new Transaction(10, "", LocalDate.now(), TransactionType.EXPENSE, TransactionCategory.BILLS);
        assertTrue(transaction.getSignedAmount() < 0);
    }
}