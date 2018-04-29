package uoc.tfg.raulberme.currencyexchange.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import uoc.tfg.raulberme.currencyexchange.dto.CurrencyDTO;
import uoc.tfg.raulberme.currencyexchange.dto.ExchangeCurrencyDTO;
import uoc.tfg.raulberme.currencyexchange.service.CurrencyExchangeService;

@RestController
@RequestMapping("/currency-exchange")
@Api(value = "CurrencyExchange", tags = { "CurrencyExchange" })
public class CurrencyExchangeAPI {

	private CurrencyExchangeService service;

	@Autowired
	public CurrencyExchangeAPI(CurrencyExchangeService service) {
		this.service = service;
	}

	@ApiOperation(value = "Get all Currencies", notes = "Returns all currencies")
	@GetMapping("/currency")
	@ResponseBody
	public Collection<CurrencyDTO> listAllCurrencies() {
		return service.listAllCurrencies();
	}

	@ApiOperation(value = "Get today ratios with optional currency's base", notes = "Returns today ratios of a optional currency as base")
	@GetMapping("ratio/today")
	@ResponseBody
	public ExchangeCurrencyDTO listRatiosByDay(@RequestParam Optional<String> baseCurrencyId) {
		return baseCurrencyId.isPresent() ? service.listRatiosByDay(baseCurrencyId.get(), LocalDate.now())
				: service.listRatiosByDay(LocalDate.now());
	}

	@ApiOperation(value = "Get today ratios with optional currency's base of a day", notes = "Returns today ratios of a optional currency as base and day")
	@GetMapping("ratio/{day}")
	@ResponseBody
	public ExchangeCurrencyDTO retrieveExchangeValue(@RequestParam Optional<String> baseCurrencyId,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
		return baseCurrencyId.isPresent() ? service.listRatiosByDay(baseCurrencyId.get(), day)
				: service.listRatiosByDay(day);
	}

	@ApiOperation(value = "Get today amount between two currencies and a quantity", notes = "Returns today amount between two given currencies and a quantity")
	@GetMapping("ratio/today/amount")
	public float calculateAmount(@RequestParam("from") String baseCurrencyId,
			@RequestParam("to") String destinationCurrencyId, @RequestParam("quantity") Float quantity) {
		return service.calculateAmount(baseCurrencyId, destinationCurrencyId, quantity, LocalDate.now());
	}

	@ApiOperation(value = "Get amount between two currencies and a quantity of a day", notes = "Returns amount between two given currencies, a quantity and a day")
	@GetMapping("ratio/{day}/amount")
	public float calculateAmount(@RequestParam("from") String baseCurrencyId,
			@RequestParam("to") String destinationCurrencyId, @RequestParam("quantity") Float quantity,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
		return service.calculateAmount(baseCurrencyId, destinationCurrencyId, quantity, day);
	}

}
