package com.example.transactioningestionservice;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import common.data.Customer;
import common.services.ResourceService;

public class CustomerService extends ResourceService<Customer> {
    public List<Customer> getAll() throws IOException {
        Stream<Customer> stream = toStream("/data/sample_contact_info.json", Customer.class);
        return stream.toList();
    }
}
