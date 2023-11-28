package com.example.notificationservice.service;

import com.rabbitmq.client.Channel;
import com.example.notificationservice.config.TransactionsExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitMQService {

  private static final String queuePrefix = "txQueue_";
  private static final Map<String, Object> quorumQueueArgs = Map.of("x-queue-type", "quorum");

  private RabbitTemplate rabbitTemplate;
  private int queues;
  private SimpleMessageListenerContainer container;

  private Logger logger = LoggerFactory.getLogger(RabbitMQService.class);

  public RabbitMQService(RabbitTemplate rabbitTemplate, @Value("${rabbitmq.queues:4}") int queues) throws IOException, TimeoutException {
    this.rabbitTemplate = rabbitTemplate;
    this.queues = queues;
    createAndBindQueues();
  }

  @Bean
  MessageListenerAdapter listenerAdapter(MessageReceiverService receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter, CustomerService customerService) throws IOException {

    logger.info("Creating RabbitMQ Simple Message Listener Container");

    container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);

    String[] queueNames = new String[queues];
    for (int i = 0; i < queues; ++i) {
      queueNames[i] = "txQueue_" + (i + 1);
    }

    container.setQueueNames(queueNames);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  // @EventListener(ApplicationReadyEvent.class)
  // void onApplicationReady() {
  // }

  private void createAndBindQueues() throws IOException, TimeoutException {
    Connection conn = rabbitTemplate.getConnectionFactory().createConnection();
    Channel ch = conn.createChannel(false);

    try {
      logger.info("RabbitMQ shall connect to: " + rabbitTemplate.getConnectionFactory().getHost());
      logger.info("Number of queues to declare: " + queues);

      List<String> queueNames = new ArrayList<>(queues);
      for (int i = 1; i <= queues; ++i)
        queueNames.add(queuePrefix + i);

      // Declare RabbitMQ queues of type quorum:
      for (String q : queueNames) {
        logger.info("Declaring RabbitMQ queue: " + q);
        ch.queueDeclare(q, true, false, false, quorumQueueArgs);
        //ch.queuePurge(q);
      }

      int i = 0;
      for (String q : queueNames) {
        ch.queueBind(q, TransactionsExchange.getName(), String.valueOf(++i));
      }

    } finally {
      ch.close();
      conn.close();
    }
  }
}
