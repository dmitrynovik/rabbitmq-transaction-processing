package com.example.transactioningestionservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class TransactionIngestionServiceApplicationTests {
    
    @MockBean
    private ApplicationService applicationService;
    
	@Test
	void contextLoads() {
	}
}
