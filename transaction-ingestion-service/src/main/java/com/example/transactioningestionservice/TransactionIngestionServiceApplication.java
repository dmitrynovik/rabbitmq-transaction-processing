package com.example.transactioningestionservice;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import common.data.*;

@SpringBootApplication
public class TransactionIngestionServiceApplication {

	public static void main(String[] args) {

		// TODO: Remove
		try {

			int customers = new CustomerService().getAll().size();
			System.out.println("CUSTOMERS: " + customers);

			int tx = new TransactionService().getAll().size();
			System.out.println("TX: " + tx);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// End Of TODO

		SpringApplication.run(TransactionIngestionServiceApplication.class, args);
	}

}
