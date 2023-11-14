package com.example.transactioningestionservice;

import java.util.concurrent.CountDownLatch;
import org.springframework.stereotype.Component;

import common.data.Transaction;

@Component
public class Receiver {
    private CountDownLatch latch = new CountDownLatch(1);

	public void receiveMessage(Transaction message) {
		System.out.println("Received <" + message.seqNumber1 + ">");
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}
