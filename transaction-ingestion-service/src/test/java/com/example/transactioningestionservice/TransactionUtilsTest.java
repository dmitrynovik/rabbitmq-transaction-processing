package com.example.transactioningestionservice;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import common.data.AtmTransaction;

public class TransactionUtilsTest {
    @Test
    void can_read_static_embedded_transactions_from_resources() throws IOException {
        List<AtmTransaction> transactions = TransactionUtils.getAll();
        Assert.isTrue(transactions.size() > 0, "no transactions found");
    }
}
