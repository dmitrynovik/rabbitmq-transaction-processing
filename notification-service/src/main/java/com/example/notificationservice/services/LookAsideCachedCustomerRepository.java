package com.example.notificationservice.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.notificationservice.data.Customer;

@Service
public class LookAsideCachedCustomerRepository {
    @Autowired
    private CustomerCache customerCache;

    @Autowired
    private CustomerRepository customerRepository;

    public Optional<Customer> findById(String id) {
        Customer cached = customerCache.cacheGet(id);
        return cached == null ? customerRepository.findById(id) : Optional.of(cached);
    }
}
