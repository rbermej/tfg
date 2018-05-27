package uoc.tfg.raulberme.usermanagement.provider.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import uoc.tfg.raulberme.usermanagement.provider.CurrencyExchangeProvider;

@Component
public class CurrencyExchangeLocalProvider implements CurrencyExchangeProvider {

	private static final String RESOURCE_URL = "http://localhost:8082/currency-exchange/";

	private final RestTemplate restTemplate;

	@Autowired
	public CurrencyExchangeLocalProvider(final RestTemplate restTemplate, final ObjectMapper mapper) {
		this.restTemplate = restTemplate;
	}

	@Override
	public boolean existsCurrency(final String currency) {
		// @formatter:off
		final String path = new StringBuilder(RESOURCE_URL)
								.append("currency/exists")
								.append("&currency=").append(currency)
								.toString();
		// @formatter:on
		try {
			return restTemplate.getForEntity(path, Boolean.class).getBody();
		} catch (final Exception e) {
			return false;
		}
	}

}
