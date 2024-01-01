package com.example.notificationservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.notificationservice.utils.RabbitMQUtils;

@SpringBootTest
class NotificationServiceApplicationTests {

    public NotificationServiceApplicationTests() {
        System.setProperty("spring.amqp.deserialization.trust.all","true");
    }

	@Test
	void contextLoads() {
	}

    @Test
    void when_hostname_is_notification_service_44_get_rabbitmq_queue_returns_44() {
        String hostname = "notification-service-44";
        Assertions.assertEquals("notification-service-44", RabbitMQUtils.getQueueName(hostname));
    }
}
