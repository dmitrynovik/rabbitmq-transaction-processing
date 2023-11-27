package com.example.notificationservice.services;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.notificationservice.data.Customer;

import common.data.AtmTransaction;

@Service
public class ConsumerService {

	Logger logger = LoggerFactory.getLogger(ConsumerService.class);

	@Autowired
	private LookAsideCachedCustomerRepository customerRepository;

	public ConsumerService() {
		logger.info("Creating consumer service");
	}

	public void receiveMessage(AtmTransaction tx) {
		String account = tx.fromAcct1 + tx.fromAcct2;
		logger.info("Received transaction <" + tx.processId + "> of " + account);
		Optional<Customer> customer = customerRepository.findById(account);
		if (customer.isEmpty()) {
			logger.info("Customer " + account + " not found");
		} 
		// TODO (Production): send this data down the Enterprise Message Hub / Persist / Do whatever
	}
}
