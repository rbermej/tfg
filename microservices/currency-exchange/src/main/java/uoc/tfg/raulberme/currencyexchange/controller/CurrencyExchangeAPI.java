package uoc.tfg.raulberme.currencyexchange.controller;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uoc.tfg.raulberme.currencyexchange.entity.Currency;
import uoc.tfg.raulberme.currencyexchange.entity.Ratio;
import uoc.tfg.raulberme.currencyexchange.service.CurrencyExchangeService;

@RestController
@RequestMapping("/currency-exchange")
public class CurrencyExchangeAPI {

	private CurrencyExchangeService service;

	@Autowired
	public CurrencyExchangeAPI(CurrencyExchangeService service) {
		super();
		this.service = service;
	}

	@GetMapping("/currency")
	public Collection<Currency> listAllCurrencies() {
		return service.listAllCurrencies();
	}

	@GetMapping("ratio/today")
	public Collection<Ratio> retrieveExchangeValue() {
		return service.listRatiosByDay(LocalDate.now());
	}

	@GetMapping("ratio/{day}")
	public Collection<Ratio> retrieveExchangeValue(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
		return service.listRatiosByDay(day);
	}
}
