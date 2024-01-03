package com.example.transactioningestionservice;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import common.data.AtmTransaction;
import common.services.ResourceUtils;

public class TransactionService extends ResourceUtils<AtmTransaction> {
    public List<AtmTransaction> getAll() throws IOException {
        Stream<AtmTransaction> stream = toStream("/data/sample_txns.json", AtmTransaction.class);
        return stream.toList();
    }
}
