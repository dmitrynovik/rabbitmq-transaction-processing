package com.example.transactioningestionservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import common.data.Transaction;

@Component
public class Runner implements CommandLineRunner {

  static final String exchangeName = "txExchange";
  static final String queuePrefix = "txQueue_";  
  static final Map<String, Object> quorumQueueArgs = Map.of("x-queue-type", "quorum");
  private RabbitTemplate rabbitTemplate;
  Logger logger = LoggerFactory.getLogger(Runner.class);
  private int queues;
  
  public Runner(@Value("${rabbitmq.queues}") int queues, RabbitTemplate rabbitTemplate) throws IOException, TimeoutException {
    this.rabbitTemplate = rabbitTemplate;
    this.queues = queues;
    createRabbitMQTopology();
  }

  private void createRabbitMQTopology() throws IOException, TimeoutException {
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
        ch.queuePurge(q);
      }

      // Declare RabbitMQ exchange of type consistent hash:
      ch.exchangeDeclare(exchangeName, "x-consistent-hash", true, false, null);

      int i = 0;
      for (String q : queueNames) {
        ch.queueBind(q, exchangeName, String.valueOf(++i));
      }
    } finally {
      ch.close();
    }
  }

  @Override
  public void run(String... args) throws InterruptedException {
    try {
      List<Transaction> tx = new TransactionService().getAll();

      for (int i = 0; i < tx.size(); ++i) {
        System.out.println("Sending transaction: " + tx.get(i).seqNumber1);
        this.rabbitTemplate.convertAndSend(exchangeName, 
            String.valueOf((i % 4) + 1),
            tx.get(i));

        Thread.sleep(1000);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Queue createQuorumQueue(String queueName) {
		return new Queue(queueName, 
      true, 
      false, 
      false, 
      quorumQueueArgs);
	}
}
