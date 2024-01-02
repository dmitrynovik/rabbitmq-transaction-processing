package com.example.notificationservice.config;

import com.example.notificationservice.repository.CustomerRepository;
import com.example.notificationservice.service.CustomerService;
import com.example.notificationservice.service.MessageReceiver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class MessageReceiverConfig {

	@Bean
	MessageReceiver messageReceiverService(CustomerService customerService, CustomerRepository customerRepository) {
		return new MessageReceiver(customerService, customerRepository);
	}
}
