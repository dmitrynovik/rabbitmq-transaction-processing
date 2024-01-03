package com.example.notificationservice.service;

import com.rabbitmq.client.Channel;

import common.CommonConfiguration.TransactionsExchange;
import com.example.notificationservice.utils.CustomerUtils;
import com.example.notificationservice.utils.RabbitMQUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitMQService {

  private static final Map<String, Object> quorumQueueArgs = Map.of("x-queue-type", "quorum");

  private RabbitTemplate rabbitTemplate;
  private SimpleMessageListenerContainer container;
  private Logger logger = LoggerFactory.getLogger(RabbitMQService.class);
  private CustomerService customerService;

  public RabbitMQService(RabbitTemplate rabbitTemplate, CustomerService customerService) throws IOException, TimeoutException {
    this.rabbitTemplate = rabbitTemplate;
    this.customerService = customerService;
    createAndBindQueues();
  }

  @Bean
  MessageListenerAdapter listenerAdapter(MessageReceiverService receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  @EventListener(ApplicationReadyEvent.class)
  public void cacheCustomers() throws IOException {
    // Load all customers into cache before consuming messages:
    CustomerUtils
      .getAll()
      .forEach(customer -> customerService.cachePut(customer));
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter, CustomerService customerService) throws IOException {

    logger.info("Creating RabbitMQ Simple Message Listener Container");

    container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);

    container.setQueueNames(RabbitMQUtils.getQueueName());
    container.setMessageListener(listenerAdapter);
    return container;
  }


  private void createAndBindQueues() throws IOException, TimeoutException {
    Connection conn = rabbitTemplate.getConnectionFactory().createConnection();
    Channel ch = conn.createChannel(false);

    try {
      logger.info("RabbitMQ shall connect to: " + rabbitTemplate.getConnectionFactory().getHost());
      String queueName = RabbitMQUtils.getQueueName();
      String routingKey = RabbitMQUtils.getRoutingKey();
 
      logger.info("Declaring RabbitMQ queue: " + queueName + " and binding using routing key: " + routingKey);
      ch.queueDeclare(queueName, true, false, false, quorumQueueArgs);
      ch.queueBind(queueName, TransactionsExchange.getName(), routingKey);

    } finally {
      ch.close();
      conn.close();
    }
  }
}
