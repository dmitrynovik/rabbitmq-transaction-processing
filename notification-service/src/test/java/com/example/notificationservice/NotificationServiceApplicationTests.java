package com.example.notificationservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NotificationServiceApplicationTests {

    public NotificationServiceApplicationTests() {
        System.setProperty("spring.amqp.deserialization.trust.all","true");
    }

	@Test
	void contextLoads() {
	}
}
