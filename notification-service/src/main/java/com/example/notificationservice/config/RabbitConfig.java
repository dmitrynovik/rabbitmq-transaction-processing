package com.example.notificationservice.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConfig {


  private RabbitTemplate rabbitTemplate;
  private int queues;
  private SimpleMessageListenerContainer container;
}
