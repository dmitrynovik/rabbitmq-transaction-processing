package com.example.notificationservice;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import common.data.Customer;
import common.services.ResourceService;

@Service
public class CustomerLoaderService extends ResourceService<Customer> {
    private static final Logger logger = LoggerFactory.getLogger(CustomerLoaderService.class);

    public CustomerLoaderService() throws IOException {
        toStream("/data/sample_contact_info.json", Customer.class)
           .forEach(customer -> cachePut(customer));
    }

    @CachePut(cacheNames = "Customers", key = "#result.accountNumber")
    public Customer cachePut(Customer customer) {
        logger.info("Caching customer: " + customer.accountNumber);
        return customer;
    }
}
