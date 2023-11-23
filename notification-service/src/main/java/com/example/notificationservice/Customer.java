package com.example.notificationservice;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Customer {

    public String isdCode;

    public String mobileNumber;

    public String langCode;

    public String emailId;

    @Id
    public String accountNumber;
}
