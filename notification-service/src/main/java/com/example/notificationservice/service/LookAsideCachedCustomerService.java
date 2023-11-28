package com.example.notificationservice.service;

import com.example.notificationservice.domain.Customer;
import com.example.notificationservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LookAsideCachedCustomerService {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    public Optional<Customer> findById(String id) {
        Customer cached = customerService.cacheGet(id);
        return cached == null ? customerRepository.findById(id) : Optional.of(cached);
    }
}
