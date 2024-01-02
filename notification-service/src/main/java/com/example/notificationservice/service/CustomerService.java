package com.example.notificationservice.service;

import com.example.notificationservice.domain.Customer;
import com.example.notificationservice.repository.CustomerRepository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Cacheable(cacheNames = "Customers",key = "#result.accountNumber")
  public Optional<Customer> cacheFind(String accountNumber){
    return customerRepository.findById(accountNumber);
  }

  @CachePut(cacheNames = "Customers", key = "#result.accountNumber")
  public Customer cachePut(Customer customer) {
    LOGGER.debug("Caching customer: " + customer.accountNumber);
    return customer;
  }

  // ONOBC: Is this just NYI?
  public Customer cacheGet(String id) {
    return null;
  }
}
