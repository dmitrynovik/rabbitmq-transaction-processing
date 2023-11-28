package com.example.notificationservice;

import common.services.ResourceService;

import com.example.notificationservice.domain.Customer;
import com.example.notificationservice.service.CustomerService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//@ComponentScan(basePackages = "com.example.notificationservice.config")
public class NotificationServiceApplication {

  public static void main(String[] args) {
	System.setProperty("spring.amqp.deserialization.trust.all","true");
    new SpringApplicationBuilder(NotificationServiceApplication.class).web(WebApplicationType.NONE).run(args);
  }

  @Bean
  ApplicationRunner applicationRunner(CustomerService customerService) {
    return (arguments) -> {
		new ResourceService<Customer>()
      		.toStream("/data/sample_contact_info.json", Customer.class)
          	.forEach(customer -> customerService.cachePut(customer));
    };
  }
}
