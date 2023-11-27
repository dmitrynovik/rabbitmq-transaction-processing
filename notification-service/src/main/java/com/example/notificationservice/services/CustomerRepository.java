package com.example.notificationservice.services;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.example.notificationservice.data.Customer;

@Repository
@Service
public interface CustomerRepository extends CrudRepository<Customer, String> {
    
}
