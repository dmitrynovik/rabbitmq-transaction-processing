package com.example.notificationservice.utils;

import java.io.IOException;
import java.util.List;

import com.example.notificationservice.domain.Customer;

import common.utils.ResourceUtils;

public class CustomerUtils {
    public static List<Customer> getAll() throws IOException {
        return ResourceUtils
            .toStream("/data/sample_contact_info.json", Customer.class)
            .toList();
    }
}
