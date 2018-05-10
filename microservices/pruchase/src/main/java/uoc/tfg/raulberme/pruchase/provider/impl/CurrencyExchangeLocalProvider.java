package uoc.tfg.raulberme.pruchase.provider.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import uoc.tfg.raulberme.pruchase.provider.CurrencyExchangeProvider;

@Component
public class CurrencyExchangeLocalProvider implements CurrencyExchangeProvider {

	private static final String RESOURCE_URL = "http://localhost:8082/currency-exchange/";

	private final RestTemplate restTemplate;

	@Autowired
	public CurrencyExchangeLocalProvider(final RestTemplate restTemplate, final ObjectMapper mapper) {
		this.restTemplate = restTemplate;
	}

	@Override
	public float calculateAmount(final String baseCurrency, final String destinationCurrency, final Float quantity) {
		// @formatter:off
		final String path = new StringBuilder(RESOURCE_URL)
								.append("ratio/today/amount")
								.append("?from=").append(baseCurrency)
								.append("&to=").append(destinationCurrency)
								.append("&quantity=").append(quantity)
								.toString();
		// @formatter:on
		return restTemplate.getForEntity(path, Float.class).getBody();
	}

}
