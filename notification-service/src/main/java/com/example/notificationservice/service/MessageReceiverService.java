package com.example.notificationservice.service;

import com.example.notificationservice.domain.Customer;

import common.data.AtmTransaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MessageReceiverService {

  @Autowired
  private LookAsideCachedCustomerService lookAsideCachedCustomerService;

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageReceiverService.class);

  public MessageReceiverService() {
    LOGGER.info("Creating consumer service");
  }

  public MessageReceiverService(LookAsideCachedCustomerService lookAsideCachedCustomerService) {
    this.lookAsideCachedCustomerService = lookAsideCachedCustomerService;
  }

  public void receiveMessage(AtmTransaction tx) {
    String account = tx.fromAcct1 + tx.fromAcct2;
    // ONOBC:I would consider moving some of the other logs (across codebase) to debug as well.
    LOGGER.debug("Received transaction <" + tx.processId + "> of " + account);
    Optional<Customer> customer = lookAsideCachedCustomerService.findById(account);
    if (customer.isEmpty()) {
      LOGGER.info("Customer " + account + " not found");
    }
    // ONOBC: Based on this comment, maybe take it a step further and call a no-op method that is protected visibilty and they
    // will know exactly where to extend this.
    // TODO (Production): send this data down the Enterprise Message Hub / Persist / Do whatever
  }
}
