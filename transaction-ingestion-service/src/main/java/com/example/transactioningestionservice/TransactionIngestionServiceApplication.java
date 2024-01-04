package com.example.transactioningestionservice;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TransactionIngestionServiceApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(TransactionIngestionServiceApplication.class)
			.web(WebApplicationType.SERVLET)
			.run(args);
	}
}
