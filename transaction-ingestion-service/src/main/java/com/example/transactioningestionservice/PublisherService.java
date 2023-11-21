package com.example.transactioningestionservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import common.data.AtmTransaction;

@Component
public class PublisherService implements CommandLineRunner {

  static final String queuePrefix = "txQueue_";
  static final String exchangeName = common.Configuration.TransactionsExchange.getName();
  static final Map<String, Object> quorumQueueArgs = Map.of("x-queue-type", "quorum");
  private RabbitTemplate rabbitTemplate;
  Logger logger = LoggerFactory.getLogger(PublisherService.class);
  private int queues;
  private int throughput;
  private boolean running;

  public void run() { running = true;}

  public void stop() { running = false; }
  
  public PublisherService(@Value("${rabbitmq.queues:4}") int queues, 
    @Value("${rabbitmq.throughput:1}") int throughput, 
    RabbitTemplate rabbitTemplate) throws IOException, TimeoutException {

    this.throughput = throughput;
    this.rabbitTemplate = rabbitTemplate;
    this.queues = queues;
    declareExchange();
  }

  private void declareExchange() throws IOException, TimeoutException {
    Channel ch = rabbitTemplate.getConnectionFactory()
      .createConnection()
      .createChannel(false);

    try {
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

      // Declare RabbitMQ exchange of type consistent hash to load balance the workload:
      ch.exchangeDeclare(exchangeName, "x-consistent-hash", true, false, null);

      int i = 0;
      for (String q : queueNames) {
        ch.queueBind(q, exchangeName, String.valueOf(++i));
      }

      this.run();
    } finally {
      ch.close();
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
