package com.example.notificationservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.client.SocketFactory;
import org.apache.geode.cache.client.proxy.ProxySocketFactories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.cache.config.EnableGemfireCaching;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;

import com.example.notificationservice.services.ConsumerService;
import com.example.notificationservice.services.CustomerCache;
import com.rabbitmq.client.Channel;

import io.micrometer.common.util.StringUtils;

@SpringBootApplication
@ClientCacheApplication
@EnableGemfireCaching
@EnablePdx
@EnableCachingDefinedRegions(clientRegionShortcut = ClientRegionShortcut.PROXY)
public class NotificationServiceApplication {
	static final String queuePrefix = "txQueue_";
  	static final Map<String, Object> quorumQueueArgs = Map.of("x-queue-type", "quorum");

	Logger logger = LoggerFactory.getLogger(NotificationServiceApplication.class);
	
	private RabbitTemplate rabbitTemplate;
	private int queues;
	private SimpleMessageListenerContainer container;

	@Autowired
	private ApplicationContext context;

	public NotificationServiceApplication(RabbitTemplate rabbitTemplate, @Value("${rabbitmq.queues:4}") int queues) throws IOException, TimeoutException {
		this.rabbitTemplate = rabbitTemplate;
		this.queues = queues;
		createAndBindQueues();
	}
	
	public static void main(String[] args) {
		System.setProperty("spring.amqp.deserialization.trust.all","true");
		SpringApplication.run(NotificationServiceApplication.class, args);
	}
	
	@Bean
    SocketFactory myProxySocketFactory(@Value("${gemfire.sni.host}") String host, @Value("${gemfire.sni.port}") int port) {
		if (port > 0 && !StringUtils.isBlank(host)) {
			logger.info("Connecting to GemFire load balancer proxy at " + host + ":" + port);
        	return ProxySocketFactories.sni(host, port);
		}
		return SocketFactory.DEFAULT;
    }

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) throws IOException {
		
		logger.info("Creating RabbitMQ Simple Message Listener Container");

		CustomerCache customerCache = context.getBean(CustomerCache.class);
		customerCache.loadAll();

		container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);

		String[] queueNames = new String[queues];
		for (int i = 0; i < queues; ++i) {
			queueNames[i] = "txQueue_" + (i + 1);
		}

		container.setQueueNames(queueNames);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	// @EventListener(ApplicationReadyEvent.class)
	// void onApplicationReady() {
	// }

	@Bean
	MessageListenerAdapter listenerAdapter(ConsumerService receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	private void createAndBindQueues() throws IOException, TimeoutException {
		Connection conn = rabbitTemplate.getConnectionFactory().createConnection();
		Channel ch = conn.createChannel(false);

		try {
			logger.info("RabbitMQ shall connect to: " + rabbitTemplate.getConnectionFactory().getHost());
			logger.info("Number of queues to declare: " + queues);

			List<String> queueNames = new ArrayList<>(queues);
			for (int i = 1; i <= queues; ++i)
				queueNames.add(queuePrefix + i);

			// Declare RabbitMQ queues of type quorum:
			for (String q : queueNames) {
				logger.info("Declaring RabbitMQ queue: " + q);
				ch.queueDeclare(q, true, false, false, quorumQueueArgs);
				//ch.queuePurge(q);
		}

		int i = 0;
		for (String q : queueNames) {
			ch.queueBind(q, common.Configuration.TransactionsExchange.getName(), String.valueOf(++i));
		}

		} finally {
			ch.close();
			conn.close();
		}
	}
}
