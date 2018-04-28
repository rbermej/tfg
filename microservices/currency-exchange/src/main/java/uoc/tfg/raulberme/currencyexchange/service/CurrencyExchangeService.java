package uoc.tfg.raulberme.currencyexchange.service;

import java.time.LocalDate;
import java.util.Collection;

import uoc.tfg.raulberme.currencyexchange.dto.CurrencyDTO;
import uoc.tfg.raulberme.currencyexchange.dto.ExchangeCurrencyDTO;
import uoc.tfg.raulberme.currencyexchange.entity.Ratio;

public interface CurrencyExchangeService {
	public Collection<CurrencyDTO> listAllCurrencies();

	public ExchangeCurrencyDTO listRatiosByDay(LocalDate day);

	public ExchangeCurrencyDTO listRatiosByDay(String currencyBase, LocalDate day);

	public Ratio retriveRatioByCurrencyAndDay(String currency, LocalDate day);

	public float calculateAmount(String currencyBase, String currencyDestination, Float quantity, LocalDate day);
}
