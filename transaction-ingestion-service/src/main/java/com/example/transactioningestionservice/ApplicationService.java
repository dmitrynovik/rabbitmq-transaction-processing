package com.example.transactioningestionservice;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private PublisherService publisherService;

    public ApplicationService(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() throws InterruptedException {
        publisherService.run();
        publisherService.startPubishing();
    }
}
