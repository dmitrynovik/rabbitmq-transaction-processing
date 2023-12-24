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

  private CustomerRepository customerRepository;
  Logger logger = LoggerFactory.getLogger(CustomerService.class);

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Cacheable(cacheNames = "Customers",key = "#result.accountNumber")
  public Optional<Customer> cacheFind(String accountNumber){
    return customerRepository.findById(accountNumber);
  }

  @CachePut(cacheNames = "Customers", key = "#result.accountNumber")
  public Customer cachePut(Customer customer) {
    logger.debug("Caching customer: " + customer.accountNumber);
    return customer;
  }

  public Customer cacheGet(String id) {
    return null;
  }
}
