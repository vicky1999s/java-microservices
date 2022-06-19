package com.vicky.microservices.currencyexchangeservice;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
	
	@GetMapping("/sample-api")
	//@Retry(name = "sample-api", fallbackMethod = "hardCodedResponse")
	@CircuitBreaker(name = "sample-api", fallbackMethod = "hardCodedResponse")
	@RateLimiter(name = "sample-api")
	@Bulkhead(name = "sample-api")
	public String sampleApi() {
		logger.info("sample API called");
		new RestTemplate().getForEntity("http://localhost:8777/summa", String.class);
		return "sample API";
	}
	
	private String hardCodedResponse(Exception s) {
		return "FallbackResponse";
	}
	
}
