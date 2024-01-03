package com.example.notificationservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record Customer (

    String isdCode,

    String mobileNumber,

    String langCode,

    String emailId,

    @Id
    String accountNumber
) { }
