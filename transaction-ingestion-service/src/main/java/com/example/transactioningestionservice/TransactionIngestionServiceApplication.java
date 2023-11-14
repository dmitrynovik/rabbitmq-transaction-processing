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

@SpringBootApplication
public class TransactionIngestionServiceApplication {

	static final String exchangeName = "txExchange";

	static final String queuePrefix = "txQueue_";

	@Bean
	CustomExchange exchange() {
		return new CustomExchange(exchangeName, "x-consistent-hash");
	}

	@Bean
	Queue[] queues() {
		Queue[] queues = new Queue[4];
		Map<String, Object> args = new HashMap<>();
		args.put("x-queue-type", "quorum");
		for (int i = 0; i < queues.length; ++i) {
			queues[i] = new Queue(queuePrefix + i, true, false, true, args);
		}
		return queues;
	}

	@Bean
	List<Binding> binding(Queue[] queues, CustomExchange exchange) {
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

	public static void main(String[] args) {
		SpringApplication.run(TransactionIngestionServiceApplication.class, args);
	}

}
