package com.example.transactioningestionservice;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import common.data.AtmTransaction;
import common.utils.ResourceUtils;

public class TransactionUtils {
    public static List<AtmTransaction> getAll() throws IOException {
        Stream<AtmTransaction> stream = ResourceUtils.toStream("/data/sample_txns.json", AtmTransaction.class);
        return stream.toList();
    }
}
