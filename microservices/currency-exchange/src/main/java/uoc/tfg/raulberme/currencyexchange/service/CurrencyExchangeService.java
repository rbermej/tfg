package uoc.tfg.raulberme.currencyexchange.service;

import java.time.LocalDate;
import java.util.Collection;

import uoc.tfg.raulberme.currencyexchange.dto.CurrencyDTO;
import uoc.tfg.raulberme.currencyexchange.dto.ExchangeCurrencyDTO;

public interface CurrencyExchangeService {

	public Collection<CurrencyDTO> listAllCurrencies(final String tokenId);

	public ExchangeCurrencyDTO listRatiosByDay(final String tokenId, final LocalDate day);

	public ExchangeCurrencyDTO listRatiosByDay(final String tokenId, final String currencyBase, final LocalDate day);

	public float calculateAmount(final String currencyBase, final String currencyDestination, final Float quantity,
			final LocalDate day);

}
