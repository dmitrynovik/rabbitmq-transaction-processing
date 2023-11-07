package com.example.transactioningestionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import common.Library;

@SpringBootApplication
public class TransactionIngestionServiceApplication {

	public static void main(String[] args) {
		Library lib = new Library();
		SpringApplication.run(TransactionIngestionServiceApplication.class, args);
	}

}
