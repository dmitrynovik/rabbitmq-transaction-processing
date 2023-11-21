package com.example.notificationservice;

import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import common.data.AtmTransaction;

@Component
public class ConsumerService {

	Logger logger = LoggerFactory.getLogger(ConsumerService.class);

	private CountDownLatch latch = new CountDownLatch(1);

	public void receiveMessage(AtmTransaction message) {
		logger.info("Received <" + message.processId + ">");
		//latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}
}
