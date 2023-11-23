package com.example.notificationservice;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
public interface CustomerRepository extends CrudRepository<Customer, String> {
    
}
