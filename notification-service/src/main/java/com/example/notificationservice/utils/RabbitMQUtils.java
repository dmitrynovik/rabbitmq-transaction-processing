package com.example.notificationservice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class RabbitMQUtils {
    public static String getQueueName() {
        String hostname = System.getenv("HOSTNAME");
        return StringUtils.isBlank(hostname) ? "tx_Queue_0" : hostname;
    }

    public static String getRoutingKey() {
        String hostname = System.getenv("HOSTNAME");
        if (StringUtils.isBlank(hostname))
            return "1";

        Pattern pattern = Pattern.compile("(\\d)$");
        Matcher matcher = pattern.matcher(hostname);
        return matcher.find() ? 
            String.valueOf(Integer.parseInt(matcher.group()) + 1) : 
            "1";
    }
}
