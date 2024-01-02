package com.example.notificationservice.service;

import com.rabbitmq.client.Channel;

import common.Configuration.TransactionsExchange;
import common.services.ResourceService;
import com.example.notificationservice.domain.Customer;
import com.example.notificationservice.utils.RabbitMQUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/*
ONOBC: In general I tend to not use @Service nor @Component and rather use @Configuration + @Bean. This leads to a simpler
model for testing as well as debugging the lifecycle. I will modify this code a bit in a subsequent commit.
 */

@Service
public class RabbitMQService {

  private static final Map<String, Object> QUOROM_QUEUE_ARGS = Map.of("x-queue-type", "quorum");

  private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQService.class);

  private final RabbitTemplate rabbitTemplate;

  private SimpleMessageListenerContainer container;

  @Autowired
  private CustomerService customerService;

  public RabbitMQService(RabbitTemplate rabbitTemplate) throws IOException, TimeoutException {
    this.rabbitTemplate = rabbitTemplate;
    createAndBindQueues();
  }

  @Bean
  MessageListenerAdapter listenerAdapter(MessageReceiverService receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  @EventListener(ApplicationReadyEvent.class)
  public void cacheCustomers() throws IOException {
    // Load all customers into cache before consuming messages:
    new ResourceService<Customer>()
      .toStream("/data/sample_contact_info.json", Customer.class)
      .forEach(customerService::cachePut);
  }

  // ONOBC: I believe this should be auto-configured for you via Boot (see https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#messaging.amqp)

  // ONOBC: I don't think you need this class at all really and can instead jsut mark MRS.receiveMessage w/ @RabbitListener
  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
    LOGGER.debug("Creating RabbitMQ Simple Message Listener Container");
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(RabbitMQUtils.getQueueName());
    container.setMessageListener(listenerAdapter);
    return container;
  }

  private void createAndBindQueues() throws IOException, TimeoutException {
    try (Connection conn = rabbitTemplate.getConnectionFactory().createConnection()) {
      try (Channel ch = conn.createChannel(false)) {

        // ONOBC: You should be able to define a Queue bean and it will be auto-created (see https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#messaging.amqp)
        LOGGER.info("RabbitMQ shall connect to: " + rabbitTemplate.getConnectionFactory().getHost());
        String queueName = RabbitMQUtils.getQueueName();
        String routingKey = RabbitMQUtils.getRoutingKey();

        LOGGER.info("Declaring RabbitMQ queue: " + queueName + " and binding using routing key: " + routingKey);
        ch.queueDeclare(queueName, true, false, false, QUOROM_QUEUE_ARGS);
        ch.queueBind(queueName, TransactionsExchange.getName(), routingKey);
      }
    }
  }
}
