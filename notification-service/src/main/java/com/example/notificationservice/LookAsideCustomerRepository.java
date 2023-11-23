package com.example.notificationservice;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LookAsideCustomerRepository {
    @Autowired
    private CustomerCache customerCache;

    @Autowired
    private CustomerRepository customerRepository;

    public Optional<Customer> findById(String id) {
        
        return customerRepository.findById(id);
    }
}
