package com.example.notificationservice.services;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.notificationservice.data.Customer;

import common.services.ResourceService;

@Service
public class CustomerCache extends ResourceService<Customer> {
    private static final Logger logger = LoggerFactory.getLogger(CustomerCache.class);

    public void loadAll() throws IOException {
        toStream("/data/sample_contact_info.json", Customer.class)
           .forEach(customer -> cachePut(customer));
    }

    @CachePut(cacheNames = "Customers", key = "#result.accountNumber")
    public Customer cachePut(Customer customer) {
        logger.info("Caching customer: " + customer.accountNumber);
        return customer;
    }

    @Cacheable(cacheNames = "Customers", key = "#id")
	public Customer cacheGet(String id) {
		return null;
	}
}
