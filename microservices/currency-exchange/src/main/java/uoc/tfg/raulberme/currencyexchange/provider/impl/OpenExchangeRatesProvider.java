package uoc.tfg.raulberme.currencyexchange.provider.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import uoc.tfg.raulberme.currencyexchange.provider.RatioProvider;

@Component
public class OpenExchangeRatesProvider implements RatioProvider {

	private static final String RESOURCE_URL = "https://openexchangerates.org/api/historical/";
	private final RestTemplate restTemplate;
	private final ObjectMapper mapper;
	private final String baseCurrencyISO;
	private final String listDestinationCurrencyISO;
	private final String appID;

	@Autowired
	public OpenExchangeRatesProvider(RestTemplate restTemplate, ObjectMapper mapper,
			@Value("${data.default.base_currency_iso}") String baseCurrencyISO,
			@Value("${data.default.destination_currency_iso}") String listDestinationCurrencyISO,
			@Value("${provider.open_exchange_rates.app_id}") String appID) {
		this.restTemplate = restTemplate;
		this.mapper = mapper;
		this.baseCurrencyISO = baseCurrencyISO;
		this.listDestinationCurrencyISO = listDestinationCurrencyISO;
		this.appID = appID;
	}

	@Override
	public Map<String, Float> findByDay(LocalDate day) {
		ResponseEntity<String> response = restTemplate.getForEntity(getPath(day), String.class);
		try {
			final String ratios = mapper.readTree(response.getBody()).path("rates").toString();
			return mapper.readValue(ratios, new TypeReference<Map<String, Float>>() {
			});
		} catch (IOException e) {
			return null;
		}
	}

	private String getPath(LocalDate day) {
		// @formatter:off
		return new StringBuilder(RESOURCE_URL)
				.append(day.toString()).append(".json")
				.append("?app_id=").append(appID)
				.append("&base=").append(baseCurrencyISO)
				.append("&symbols=").append(listDestinationCurrencyISO)
				.toString();
		// @formatter:on
	}

}
