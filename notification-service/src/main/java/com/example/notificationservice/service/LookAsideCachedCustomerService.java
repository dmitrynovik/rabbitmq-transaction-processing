package com.example.notificationservice.service;

import com.example.notificationservice.domain.Customer;
import com.example.notificationservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LookAsideCachedCustomerService {

    // ONOBC - everytime field injection is used, a fairy is killed in the forest ;)
    //@Autowired
    private final CustomerService customerService;

    //@Autowired
    private final CustomerRepository customerRepository;

    public LookAsideCachedCustomerService(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(customerService.cacheGet(id))
                .or(() -> customerRepository.findById(id));
    }
}
