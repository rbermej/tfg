package uoc.tfg.raulberme.currencyexchange.service;

import java.time.LocalDate;
import java.util.Collection;

import uoc.tfg.raulberme.currencyexchange.entity.Currency;
import uoc.tfg.raulberme.currencyexchange.entity.Ratio;
import uoc.tfg.raulberme.currencyexchange.rest.ExchangeCurrencyRest;

public interface CurrencyExchangeService {
	public Collection<Currency> listAllCurrencies();

	public ExchangeCurrencyRest listRatiosByDay(LocalDate day);

	public ExchangeCurrencyRest listRatiosByDay(Long currencyBase, LocalDate day);

	public Ratio retriveRatiosByCurrencyAndDay(Long currency, LocalDate day);

	public float calculateAmount(Long currencyBase, Long currencyDestination, Float quantity, LocalDate day);
}
