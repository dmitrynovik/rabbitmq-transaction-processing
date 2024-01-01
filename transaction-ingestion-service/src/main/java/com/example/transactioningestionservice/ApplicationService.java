package com.example.transactioningestionservice;

import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    public ApplicationService(PublisherService publisherService) throws InterruptedException {
        publisherService.run();
        publisherService.startPubishing();
    }
}
