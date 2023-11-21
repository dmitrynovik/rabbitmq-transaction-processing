package com.example.notificationservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

import common.data.AtmTransaction;

@Component
public class ConsumerService {
	static final String queuePrefix = "txQueue_";
  	static final Map<String, Object> quorumQueueArgs = Map.of("x-queue-type", "quorum");

	Logger logger = LoggerFactory.getLogger(ConsumerService.class);
	private RabbitTemplate rabbitTemplate;
	private int queues;

	public ConsumerService(RabbitTemplate rabbitTemplate, @Value("${rabbitmq.queues:4}") int queues) 
		throws IOException, TimeoutException {

		this.rabbitTemplate = rabbitTemplate;
		this.queues = queues;
		createAndBindQueues();
	}

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
			ch.queueBind(q, common.Configuration.TransactionsExchange.getName(), String.valueOf(++i));
		}

		} finally {
			ch.close();
			conn.close();
		}
	}

	private CountDownLatch latch = new CountDownLatch(1);

	public void receiveMessage(AtmTransaction message) {
		logger.info("Received <" + message.processId + ">");
		//latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}
