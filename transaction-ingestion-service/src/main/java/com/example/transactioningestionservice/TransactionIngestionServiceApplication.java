package com.example.transactioningestionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import java.util.Map;
// import org.springframework.context.annotation.Bean;
// import org.springframework.amqp.core.CustomExchange;
// import org.springframework.amqp.rabbit.connection.ConnectionFactory;
// import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
// import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

@SpringBootApplication
public class TransactionIngestionServiceApplication {

	//static final Map<String, Object> queueArgs = Map.of("x-queue-type", "quorum");

	// @Bean
	// CustomExchange exchange() {
	// 	System.out.println("CREATE EXCHANGE " + exchangeName);
	// 	return new CustomExchange(exchangeName, "x-consistent-hash");
	// }


	// @Bean Queue queue1() {
	// 	return new Queue(queuePrefix + 1, true, false, false, queueArgs);
	// }

	// @Bean Binding binding1(Queue queue1, CustomExchange exchange) {
	// 	return BindingBuilder.bind(queue1)
	// 			.to(exchange)
	// 			.with("1")
	// 			.noargs();
	// }

	// @Bean Binding binding2(Queue queue2, CustomExchange exchange) {
	// 	return BindingBuilder.bind(queue2)
	// 			.to(exchange)
	// 			.with("2")
	// 			.noargs();
	// }

	// @Bean Binding binding3(Queue queue3, CustomExchange exchange) {
	// 	return BindingBuilder.bind(queue3)
	// 			.to(exchange)
	// 			.with("3")
	// 			.noargs();
	// }

	// @Bean Binding binding4(Queue queue4, CustomExchange exchange) {
	// 	return BindingBuilder.bind(queue4)
	// 			.to(exchange)
	// 			.with("4")
	// 			.noargs();
	// }

	// @Bean Queue queue2() {
	// 	return new Queue(queuePrefix + 2, true, false, false, queueArgs);
	// }

	// @Bean Queue queue3() {
	// 	return new Queue(queuePrefix + 3, true, false, false, queueArgs);
	// }

	// @Bean Queue queue4() {
	// 	return new Queue(queuePrefix + 4, true, false, false, queueArgs);
	// }

	// @Bean
	// SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
	// 		MessageListenerAdapter listenerAdapter) {
		
	// 	System.out.println("CREATING CONTAINER");
	// 	SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
	// 	container.setConnectionFactory(connectionFactory);
	// 	container.setQueueNames("txQueue_1", "txQueue_2", "txQueue_3", "txQueue_4");
	// 	container.setMessageListener(listenerAdapter);
	// 	return container;
	// }

	// @Bean
	// MessageListenerAdapter listenerAdapter(Receiver receiver) {
	// 	return new MessageListenerAdapter(receiver, "receiveMessage");
	// }

	public static void main(String[] args) {
		System.setProperty("spring.amqp.deserialization.trust.all","true");
		SpringApplication.run(TransactionIngestionServiceApplication.class, args);
	}

}
