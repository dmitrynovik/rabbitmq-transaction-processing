package com.example.notificationservice;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class NotificationServiceApplication {

	// ONOBC: I typically set these in build.gradle
  public static void main(String[] args) {
	  System.setProperty("spring.amqp.deserialization.trust.all","true");

    new SpringApplicationBuilder(NotificationServiceApplication.class)
      .web(WebApplicationType.SERVLET)
      .run(args);
  }
}
