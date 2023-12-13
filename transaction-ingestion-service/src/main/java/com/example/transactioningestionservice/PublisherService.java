package com.example.transactioningestionservice;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

import common.data.AtmTransaction;

@Service
public class PublisherService implements CommandLineRunner {

  static final String exchangeName = common.Configuration.TransactionsExchange.getName();
  
  private RabbitTemplate rabbitTemplate;
  Logger logger = LoggerFactory.getLogger(PublisherService.class);
  private int throughput;
  private boolean running;


  public void run() { running = true;}

  public void stop() { running = false; }
  
  public PublisherService(@Value("${rabbitmq.throughput:1}") int throughput,
    RabbitTemplate rabbitTemplate) throws IOException, TimeoutException {
      logger.info("Connecting to the RabbitMQ at: " + rabbitTemplate.getConnectionFactory().getHost());

      this.throughput = throughput;
      this.rabbitTemplate = rabbitTemplate;
      declareExchange();
  }

  @EventListener(ApplicationReadyEvent.class)
  public void doSomethingAfterStartup() {
      logger.info("The application has started up");
  }

  @EventListener
  public void onEvent(AvailabilityChangeEvent<LivenessState> event) {
      logger.info("Availability changed to: " + event.getState());
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

  public void run(String... args) throws InterruptedException {
    Runnable runnable = () -> {
      try {
        runImpl();
      } catch (InterruptedException e) {
        logger.error("Error executing the runner: %s", e);
      }
    };
    
    Thread backgroundJob = new Thread(runnable);
    backgroundJob.start();
  }

  public void runImpl() throws InterruptedException {
    try {
      // pull a batch of transactions (poC: data embedded as a resource, production: Database)
      List<AtmTransaction> atmTransactions = new TransactionService().getAll();

      int i = 0;
      while (running) {
        //
        // Publish a batch of the "throughput" size of the atmTransactions per second:
        // 
        for (int j = i; j < i + throughput; ++j) {
          AtmTransaction atmTransaction = atmTransactions.get(j % atmTransactions.size());
          String routingKey = atmTransaction.processId;
          logger.info("(" + j + ") Sending transaction: " + atmTransaction.processId + ", routingKey = " + routingKey);

          this.rabbitTemplate.convertAndSend(exchangeName, 
              routingKey,
              atmTransaction);
        }

        // Pause for 1 second:
        i += throughput;
        i = i % atmTransactions.size();
        Thread.sleep(1000);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
