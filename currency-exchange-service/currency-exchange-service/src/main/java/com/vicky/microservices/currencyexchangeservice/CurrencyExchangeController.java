package com.vicky.microservices.currencyexchangeservice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {
	
	private static final Logger logger = LoggerFactory.getLogger(CurrencyExchange.class);
	
	@Autowired
	private CurrencyExchangeRepo repo;
	
	@Autowired
	private Environment environment;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retriveExchangeValue(@PathVariable String from, @PathVariable String to) {
		//CurrencyExchange currencyExchange = new CurrencyExchange(1000L, from, to, BigDecimal.valueOf(50));
		CurrencyExchange currencyExchange = repo.findByFromAndTo(from, to);
		if(currencyExchange==null)
			throw new RuntimeException("Unable to find data for " + from + " " + to);
		String port = environment.getProperty("local.server.port");
		currencyExchange.setEnvironment(port);
		logger.info("retriveExchange value called with {} to {}", from, to);
		return currencyExchange;
		
	}
	
}
