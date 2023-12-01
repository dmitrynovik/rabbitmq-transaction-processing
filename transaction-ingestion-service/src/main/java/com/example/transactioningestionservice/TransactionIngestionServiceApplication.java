package com.example.transactioningestionservice;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TransactionIngestionServiceApplication {
	public static void main(String[] args) {
		System.setProperty("spring.amqp.deserialization.trust.all","true");

		SpringApplication.run(TransactionIngestionServiceApplication.class, args);

		// new SpringApplicationBuilder(TransactionIngestionServiceApplication.class)
		// 	.web(WebApplicationType.NONE)
		// 	.run(args);
	}

	@Bean
  ReactiveHealthContributor coreServices() {
    final Map<String, ReactiveHealthIndicator> registry = new LinkedHashMap<>();
    return CompositeReactiveHealthContributor.fromMap(registry);
  }

}
