package com.example.transactioningestionservice.config;

import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import common.data.AtmTransaction;

@Configuration(proxyBeanMethods = false)
public class RabbitMQConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        DefaultClassMapper defaultClassMapper = new DefaultClassMapper();
        String pack = AtmTransaction.class.getPackage().getName();
        defaultClassMapper.setTrustedPackages(pack); // trusted packages
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setClassMapper(defaultClassMapper);
        return jackson2JsonMessageConverter;
    }
}
