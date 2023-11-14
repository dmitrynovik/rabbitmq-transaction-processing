package com.example.transactioningestionservice;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Runner implements CommandLineRunner {

  private RabbitTemplate rabbitTemplate;

  public Runner(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void run(String... args) {
    try {
      System.out.println("RABBIT QUEUE: " + this.rabbitTemplate.getDefaultReceiveQueue());


      int customers = new CustomerService().getAll().size();
      System.out.println("CUSTOMERS: " + customers);

      int tx = new TransactionService().getAll().size();
      System.out.println("TX: " + tx);
//
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
