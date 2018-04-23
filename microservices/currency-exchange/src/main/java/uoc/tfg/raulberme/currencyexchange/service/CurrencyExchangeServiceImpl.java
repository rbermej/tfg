package uoc.tfg.raulberme.currencyexchange.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import uoc.tfg.raulberme.currencyexchange.entity.Currency;
import uoc.tfg.raulberme.currencyexchange.entity.Ratio;
import uoc.tfg.raulberme.currencyexchange.repository.CurrencyRepository;
import uoc.tfg.raulberme.currencyexchange.repository.RatioRepository;
import uoc.tfg.raulberme.currencyexchange.rest.CurrencyWithRatioRest;
import uoc.tfg.raulberme.currencyexchange.rest.ExchangeCurrencyRest;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

	private final Long baseCurrencyId;
	private final CurrencyRepository currencyRepository;
	private final RatioRepository ratioRepository;

	@Autowired
	public CurrencyExchangeServiceImpl(@Value("${data.default.base_currency}") Long baseCurrencyId,
			CurrencyRepository currencyRepository, RatioRepository ratioRepository) {
		super();
		this.baseCurrencyId = baseCurrencyId;
		this.currencyRepository = currencyRepository;
		this.ratioRepository = ratioRepository;
	}

	@Override
	public Collection<Currency> listAllCurrencies() {
		return currencyRepository.findAll();
	}

	@Override
	public ExchangeCurrencyRest listRatiosByDay(LocalDate day) {
		return listRatiosByDay(baseCurrencyId, day);
	}

	@Override
	public ExchangeCurrencyRest listRatiosByDay(Long baseCurrencyId, LocalDate day) {

		final Collection<Ratio> listRatios = ratioRepository.findByDay(day);

		if (listRatios.isEmpty()) {
			return null;
		} else {

			CurrencyWithRatioRest baseCurrency = null;

			final Collection<CurrencyWithRatioRest> destinationCurrency = listRatios.stream()
					.map(r -> CurrencyWithRatioRest.builder().id(r.getCurrency().getId())
							.name(r.getCurrency().getName()).acronym(r.getCurrency().getAcronym())
							.ratio(r.getRatioExchange()).build())
					.collect(Collectors.toList());

			if (this.baseCurrencyId.equals(baseCurrencyId)) {
				baseCurrency = defaultCurrencyWithRatio();
			} else {
				destinationCurrency.add(defaultCurrencyWithRatio());

				final Optional<CurrencyWithRatioRest> baseRatio = destinationCurrency.stream()
						.filter(r -> baseCurrencyId.equals(r.getId())).findFirst();
				if (baseRatio.isPresent()) {
					baseCurrency = baseRatio.get();
					destinationCurrency.remove(baseCurrency);
					destinationCurrency.forEach(
							(final CurrencyWithRatioRest r) -> r.setRatio(r.getRatio() / baseRatio.get().getRatio()));
				}
			}

			final Collection<CurrencyWithRatioRest> orderedDestinationList = destinationCurrency.stream()
					.sorted((r1, r2) -> r1.getName().compareToIgnoreCase(r2.getName())).collect(Collectors.toList());

			return ExchangeCurrencyRest.builder().baseCurrency(baseCurrency).day(day)
					.destinationCurrency(orderedDestinationList).build();
		}

	}

	@Override
	public Ratio retriveRatiosByCurrencyAndDay(Long currency, LocalDate day) {
		return ratioRepository.findByCurrencyAndDay(currency, day);
	}

	@Override
	public float calculateAmount(Long baseCurrencyId, Long destinationCurrencyId, Float quantity, LocalDate day) {
		// TODO Auto-generated method stub
		return 1;
	}

	private CurrencyWithRatioRest defaultCurrencyWithRatio() {
		final Currency currency = currencyRepository.getOne(baseCurrencyId);
		return CurrencyWithRatioRest.builder().id(currency.getId()).name(currency.getName())
				.acronym(currency.getAcronym()).ratio(Float.valueOf("1")).build();
	}

}
