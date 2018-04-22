package uoc.tfg.raulberme.currencyexchange.service;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import uoc.tfg.raulberme.currencyexchange.entity.Currency;
import uoc.tfg.raulberme.currencyexchange.entity.Ratio;
import uoc.tfg.raulberme.currencyexchange.repository.CurrencyRepository;
import uoc.tfg.raulberme.currencyexchange.repository.RatioRepository;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

	private final Long baseCurrency;
	private final CurrencyRepository currencyRepository;
	private final RatioRepository ratioRepository;

	@Autowired
	public CurrencyExchangeServiceImpl(@Value("${data.default.base_currency}") Long baseCurrency,
			CurrencyRepository currencyRepository, RatioRepository ratioRepository) {
		super();
		this.baseCurrency = baseCurrency;
		this.currencyRepository = currencyRepository;
		this.ratioRepository = ratioRepository;
	}

	@Override
	public Collection<Currency> listAllCurrencies() {
		return currencyRepository.findAll();
	}

	@Override
	public Collection<Ratio> listRatiosByDay(LocalDate day) {
		final Collection<Ratio> listRatios = ratioRepository.findByDay(day);
		if (!listRatios.isEmpty())
			return listRatios;
		else {
			// getRatiosFromProvider(day);
			System.out.println("Lista Vacía " + baseCurrency);
		}
		return listRatios;
	}

	@Override
	public Collection<Ratio> listRatiosByDay(Long baseCurrency, LocalDate day) {
		final Collection<Ratio> listRatios = ratioRepository.findByDay(day);
		if (!listRatios.isEmpty())
			return listRatios;
		else {
			// getRatiosFromProvider(day);
			System.out.println("LISTA_VACIA " + baseCurrency);
		}
		// Cambiar base
		if (!this.baseCurrency.equals(baseCurrency)) {
			final Float baseRatioExchange = listRatios.stream().filter(r -> baseCurrency.equals(r.getCurrency()))
					.findFirst().get().getRatioExchange();
			listRatios.forEach(
					(final Ratio ratio) -> ratio.setRatioExchange(ratio.getRatioExchange() / baseRatioExchange));
		}
		return listRatios;
	}

	@Override
	public Ratio retriveRatiosByCurrencyAndDay(Long currency, LocalDate day) {
		return ratioRepository.findByCurrencyAndDay(currency, day);
	}

	@Override
	public float calculateAmount(Long baseCurrency, Long destinationCurrency, Float quantity, LocalDate day) {
		// TODO Auto-generated method stub
		return 0;
	}

}
