package com.example.notificationservice;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class NotificationServiceApplication {

  public static void main(String[] args) {

    new SpringApplicationBuilder(NotificationServiceApplication.class)
      .web(WebApplicationType.SERVLET)
      .run(args);
  }
}
