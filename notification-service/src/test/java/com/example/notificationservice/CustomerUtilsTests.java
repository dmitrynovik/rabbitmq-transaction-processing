package com.example.notificationservice;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import com.example.notificationservice.domain.Customer;
import com.example.notificationservice.utils.CustomerUtils;

public class CustomerUtilsTests {
    @Test
    void can_read_static_embedded_transactions_from_resources() throws IOException {
        List<Customer> transactions = CustomerUtils.getAll();
        Assert.isTrue(transactions.size() > 0, "no customers found in resources");
    }
}
