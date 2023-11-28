package com.example.notificationservice.repository;

import com.example.notificationservice.domain.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer,String> {
}
