package com.example.transactioningestionservice;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import common.data.Transaction;

import java.io.IOException;
import java.util.List;

@Component
public class Runner implements CommandLineRunner {

  private RabbitTemplate rabbitTemplate;

  public Runner(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void run(String... args) throws InterruptedException {
    try {
      System.out.println("RABBIT QUEUE: " + this.rabbitTemplate.getDefaultReceiveQueue());

      //   int customers = new CustomerService().getAll().size();
      //   System.out.println("CUSTOMERS: " + customers);

      List<Transaction> tx = new TransactionService().getAll();
      for (int i = 0; i < tx.size(); ++i) {
        System.out.println("Sending customer: " + tx.get(i).seqNumber1);
        this.rabbitTemplate.convertAndSend(TransactionIngestionServiceApplication.exchangeName, 
            "", 
            tx.get(i));

        Thread.sleep(1000);
      }



    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
