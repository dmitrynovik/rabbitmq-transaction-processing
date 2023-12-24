package com.example.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext;

        Logger logger = LoggerFactory.getLogger(ApplicationContextProvider.class);
        Environment env = applicationContext.getEnvironment();

        logger.info("Property spring.cloud.kubernetes.enabled: " + env.getProperty("spring.cloud.kubernetes.enabled"));
        logger.info("Property spring.cloud.kubernetes.config.enabled: " + env.getProperty("spring.cloud.kubernetes.config.enabled"));
        logger.info("Property spring.cloud.kubernetes.config.name: " + env.getProperty("spring.cloud.kubernetes.config.name"));
        logger.info("Property spring.cloud.kubernetes.config.namespace: " + env.getProperty("spring.cloud.kubernetes.config.namespace"));
        logger.info("Property spring.cloud.kubernetes.reload.enabled: " + env.getProperty("spring.cloud.kubernetes.reload.enabled"));
        logger.info("Property spring.application.name: " + env.getProperty("spring.application.name"));
        logger.info("Property spring.rabbitmq.host: " + env.getProperty("spring.rabbitmq.host"));
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
