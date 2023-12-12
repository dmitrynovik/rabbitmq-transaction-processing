package com.example.transactioningestionservice;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TransactionIngestionServiceApplication {

	public static void main(String[] args) {
		System.setProperty("spring.amqp.deserialization.trust.all","true");

		new SpringApplicationBuilder(TransactionIngestionServiceApplication.class)
			.web(WebApplicationType.SERVLET)
			.run(args);
	}
}
