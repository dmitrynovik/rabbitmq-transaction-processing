package com.example.transactioningestionservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import common.data.AtmTransaction;

@Component
public class PublisherService implements CommandLineRunner {

  static final String exchangeName = common.Configuration.TransactionsExchange.getName();
  
  private RabbitTemplate rabbitTemplate;
  Logger logger = LoggerFactory.getLogger(PublisherService.class);
  private int throughput;
  private boolean running;
  private int queues;

  public void run() { running = true;}int i = 0;

  public void stop() { running = false; }
  
  public PublisherService(@Value("${rabbitmq.throughput:1}") int throughput, @Value("${rabbitmq.queues:4}") int queues,
    RabbitTemplate rabbitTemplate) throws IOException, TimeoutException {

    this.queues = queues;
    this.throughput = throughput;
    this.rabbitTemplate = rabbitTemplate;
    declareExchange();
  }

  private void declareExchange() throws IOException, TimeoutException {
    Connection conn = rabbitTemplate.getConnectionFactory().createConnection();
		Channel ch = conn.createChannel(false);

    try {
      // Declare RabbitMQ exchange of type consistent hash to load balance the workload:
      ch.exchangeDeclare(exchangeName, "x-consistent-hash", true, false, null);
      this.run();

    } finally {
      ch.close();
      conn.close();
    }
  }

  @Override
  public void run(String... args) throws InterruptedException {
    try {
      // pull a batch of transactions (poC: data embedded as a resource, production: Database)
      List<AtmTransaction> atmTransactions = new TransactionService().getAll();

      while (running) {

        //
        // Publish "throughput" atmTransactions per second:
        // 
        for (int i = 0; i < throughput; ++i) {
        logger.info("Sending transaction: " + atmTransactions.get(i % atmTransactions.size()).processId);

        this.rabbitTemplate.convertAndSend(exchangeName, 
            String.valueOf((i % queues) + 1), // routing key
            atmTransactions.get(i));

        // Pause for 1 second:
        Thread.sleep(1000);
      }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
