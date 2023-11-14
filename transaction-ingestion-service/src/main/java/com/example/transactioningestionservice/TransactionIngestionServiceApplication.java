package com.example.transactioningestionservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

@SpringBootApplication
public class TransactionIngestionServiceApplication {

	static final String exchangeName = "txExchange";
	static final String queuePrefix = "txQueue_";
	static final Map<String, Object> queueArgs = new HashMap<>();

	public TransactionIngestionServiceApplication() {
		queueArgs.put("x-queue-type", "quorum");
	}

	@Bean
	CustomExchange exchange() {
		System.out.println("CREATE EXCHANGES...");
		return new CustomExchange(exchangeName, "x-consistent-hash");
	}

	@Bean
	Queue[] queues() {
		System.out.println("CREATE QUEUES...");
		Queue[] queues = new Queue[4];
		Map<String, Object> args = new HashMap<>();
		args.put("x-queue-type", "quorum");
		for (int i = 0; i < queues.length; ++i) {
			queues[i] = new Queue(queuePrefix + i, true, false, true, args);
		}
		return queues;
	}

	@Bean Queue queue1() {
		return new Queue(queuePrefix + 1, true, false, false, queueArgs);
	}

	@Bean Binding binding1(Queue queue1, CustomExchange exchange) {
		return BindingBuilder.bind(queue1)
				.to(exchange)
				.with("1")
				.noargs();
	}

	@Bean Binding binding2(Queue queue2, CustomExchange exchange) {
		return BindingBuilder.bind(queue2)
				.to(exchange)
				.with("2")
				.noargs();
	}

	@Bean Binding binding3(Queue queue3, CustomExchange exchange) {
		return BindingBuilder.bind(queue3)
				.to(exchange)
				.with("3")
				.noargs();
	}

	@Bean Binding binding4(Queue queue4, CustomExchange exchange) {
		return BindingBuilder.bind(queue4)
				.to(exchange)
				.with("4")
				.noargs();
	}

	@Bean Queue queue2() {
		return new Queue(queuePrefix + 2, true, false, false, queueArgs);
	}

	@Bean Queue queue3() {
		return new Queue(queuePrefix + 3, true, false, false, queueArgs);
	}

	@Bean Queue queue4() {
		return new Queue(queuePrefix + 4, true, false, false, queueArgs);
	}

	@Bean
	List<Binding> binding(Queue[] queues, CustomExchange exchange) {
		System.out.println("CREATE BINDINGS...");
		List<Binding> bindings = new ArrayList<>();
		for (int i = 0; i < queues.length; ++i) {
			Binding binding = BindingBuilder.bind(queues[i])
				.to(exchange)
				.with("")
				.noargs();

			bindings.add(binding);
		}
		return bindings;
	}
	
	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		
		System.out.println("CREATING CONTAINER");
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames("txQueue_1", "txQueue_2", "txQueue_3", "txQueue_4");
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	public static void main(String[] args) {
		System.setProperty("spring.amqp.deserialization.trust.all","true");
		SpringApplication.run(TransactionIngestionServiceApplication.class, args);
	}

}
