package com.example.notificationservice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class RabbitMQUtils {

    private static final Pattern ROUTING_KEY_PATTERN = Pattern.compile("(\\d)$");

    private static final String DEFAULT_ROUTING_KEY = "1";

    public static String getQueueName() {
        String hostname = System.getenv("HOSTNAME");
        return getQueueName(hostname);
    }

    public static String getQueueName(String hostname) {
        return !StringUtils.hasText(hostname) ? "tx_Queue_0" : hostname;
    }

    public static String getRoutingKey() {
        String hostname = System.getenv("HOSTNAME");
        if (!StringUtils.hasText(hostname)) {
            return DEFAULT_ROUTING_KEY;
        }

        Matcher matcher = ROUTING_KEY_PATTERN.matcher(hostname);
        return matcher.find() ?
            String.valueOf(Integer.parseInt(matcher.group()) + 1) : 
            DEFAULT_ROUTING_KEY;
    }
}
