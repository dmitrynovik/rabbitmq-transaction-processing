package com.example.notificationservice.service;

import java.io.IOException;
import java.util.Optional;

import com.example.notificationservice.domain.Customer;
import com.example.notificationservice.repository.CustomerRepository;
import common.data.AtmTransaction;
import common.services.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

public class MessageReceiver {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiver.class);
  private final CustomerService customerService;
  private final CustomerRepository customerRepository;

  public MessageReceiver(CustomerService customerService, CustomerRepository customerRepository) {
    this.customerService = customerService;
    this.customerRepository = customerRepository;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void cacheCustomers() throws IOException {
    // Load all customers into cache before consuming messages:
    new ResourceService<Customer>()
            .toStream("/data/sample_contact_info.json", Customer.class)
            .forEach(customerService::cachePut);
  }

  // ONOBC: take a look at the available queuesToDeclare attribute
  @RabbitListener(queues = "WhateverYourQueueNameIs")
  public void receiveMessage(AtmTransaction tx) {
    String accountId = tx.fromAcct1 + tx.fromAcct2;
    LOGGER.debug("Received transaction <" + tx.processId + "> of " + accountId);
    Optional<Customer> customer = findCustomerById(accountId);
    if (customer.isEmpty()) {
      LOGGER.info("Customer " + accountId + " not found");
    }
    // ONOBC: Based on this comment, maybe take it a step further and call a no-op method that is protected visibilty and they
    // will know exactly where to extend this.
    // TODO (Production): send this data down the Enterprise Message Hub / Persist / Do whatever
  }

  private Optional<Customer> findCustomerById(String id) {
    return Optional.ofNullable(customerService.cacheGet(id))
            .or(() -> customerRepository.findById(id));
  }
}
