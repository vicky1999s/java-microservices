package com.vicky.microservices.currencyexchangeservice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CurrencyExchangeController {
	
	private static final Logger logger = LoggerFactory.getLogger(CurrencyExchange.class);
	
	@Autowired
	private CurrencyExchangeRepo repo;
	
	@Autowired
	private Environment environment;
	
	@Retry(name = "currency-exchange", fallbackMethod = "currencyExchangeFallback")
	@CircuitBreaker(name = "currency-exchange", fallbackMethod = "currencyExchangeFallback")
	@RateLimiter(name = "currency-exchange")
	@Bulkhead(name = "currency-exchange")
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retriveExchangeValue(@PathVariable String from, @PathVariable String to) {
		CurrencyExchange currencyExchange = repo.findByFromAndTo(from, to);
		if(currencyExchange==null)
			throw new RuntimeException("Unable to find data for " + from + " " + to);
		String port = environment.getProperty("local.server.port");
		currencyExchange.setEnvironment(port);
		logger.info("retriveExchange value called with {} to {}", from, to);
		return currencyExchange;
		
	}
	
	private CurrencyExchange currencyExchangeFallback(Exception e) {
		logger.error("Currency exchange server error");
		return null;
	}
	
}
