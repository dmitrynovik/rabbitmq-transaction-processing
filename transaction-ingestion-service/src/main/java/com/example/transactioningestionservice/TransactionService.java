package com.example.transactioningestionservice;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import common.data.Transaction;
import common.services.ResourceService;

public class TransactionService extends ResourceService<Transaction> {
    public List<Transaction> getAll() throws IOException {
        Stream<Transaction> stream = toStream("/data/sample_txns.json", Transaction.class);
        return stream.toList();
    }
}
