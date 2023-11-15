package com.example.transactioningestionservice;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import common.data.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
public class Runner implements CommandLineRunner {

  static final String exchangeName = "txExchange";
  static final String queuePrefix = "txQueue_";  
  static final Map<String, Object> quorumQueueArgs = Map.of("x-queue-type", "quorum");
  private RabbitTemplate rabbitTemplate;
  private int queues = 4;

  public Runner(RabbitTemplate rabbitTemplate) throws IOException, TimeoutException {
    this.rabbitTemplate = rabbitTemplate;
    createRabbitMQTopology();
  }

  private void createRabbitMQTopology() throws IOException, TimeoutException {
    Channel ch = rabbitTemplate.getConnectionFactory()
      .createConnection()
      .createChannel(false);

    try {
      // TODO: properties
      queues = 4;//Integer.parseInt( System.getProperty("rabbitmq.queues") );
      
      List<String> queueNames = new ArrayList<>(queues);
      for (int i = 1; i <= queues; ++i)
        queueNames.add(queuePrefix + i);

      // Declare RabbitMQ queues of type quorum:
      for (String q : queueNames) {
        System.out.println("DECLARING QUEUE " + q);
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
