package uoc.tfg.raulberme.currencyexchange.service;

import java.time.LocalDate;
import java.util.Collection;

import uoc.tfg.raulberme.currencyexchange.dto.CurrencyDTO;
import uoc.tfg.raulberme.currencyexchange.dto.ExchangeCurrencyDTO;

public interface CurrencyExchangeService {

	public Collection<CurrencyDTO> listAllCurrencies();

	public ExchangeCurrencyDTO listRatiosByDay(final LocalDate day);

	public ExchangeCurrencyDTO listRatiosByDay(final String currencyBase, final LocalDate day);

	public float calculateAmount(final String currencyBase, final String currencyDestination, final Float quantity,
			final LocalDate day);

}
