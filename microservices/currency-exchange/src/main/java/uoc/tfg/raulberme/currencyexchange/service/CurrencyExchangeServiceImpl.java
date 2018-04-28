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
import uoc.tfg.raulberme.currencyexchange.rest.RatioRest;
import uoc.tfg.raulberme.currencyexchange.rest.ExchangeCurrencyRest;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

	private static final Float SYSTEM_BASE_RATIO_EXCHANGE = Float.valueOf("1");
	private final String systemBaseCurrencyId;
	private final CurrencyRepository currencyRepository;
	private final RatioRepository ratioRepository;

	@Autowired
	public CurrencyExchangeServiceImpl(@Value("${data.default.base_currency_iso}") String systemBaseCurrencyId,
			CurrencyRepository currencyRepository, RatioRepository ratioRepository) {
		super();
		this.systemBaseCurrencyId = systemBaseCurrencyId;
		this.currencyRepository = currencyRepository;
		this.ratioRepository = ratioRepository;
	}

	@Override
	public Collection<Currency> listAllCurrencies() {
		return currencyRepository.findAll();
	}

	@Override
	public ExchangeCurrencyRest listRatiosByDay(LocalDate day) {
		return listRatiosByDay(systemBaseCurrencyId, day);
	}

	@Override
	public ExchangeCurrencyRest listRatiosByDay(String baseCurrencyId, LocalDate day) {

		final Collection<Ratio> listRatios = ratioRepository.findByDay(day);

		if (listRatios.isEmpty()) {
			return null;
		} else {

			RatioRest baseCurrency = null;

			final Collection<RatioRest> destinationCurrency = listRatios.stream()
					.map(r -> RatioRest.builder().id(r.getId()).isoCode(r.getCurrency().getIsoCode())
							.name(r.getCurrency().getName()).ratio(r.getRatioExchange()).build())
					.collect(Collectors.toList());

			if (systemBaseCurrencyId.equals(baseCurrencyId)) {
				baseCurrency = defaultRatioRest();
			} else {
				destinationCurrency.add(defaultRatioRest());

				final Optional<RatioRest> baseRatio = destinationCurrency.stream()
						.filter(r -> baseCurrencyId.equals(r.getId())).findFirst();
				if (baseRatio.isPresent()) {
					baseCurrency = baseRatio.get();
					destinationCurrency.remove(baseCurrency);
					destinationCurrency.add(defaultRatioRest());
					destinationCurrency
							.forEach((final RatioRest r) -> r.setRatio(r.getRatio() / baseRatio.get().getRatio()));
				}
			}

			final Collection<RatioRest> orderedDestinationList = destinationCurrency.stream()
					.sorted((r1, r2) -> r1.getName().compareToIgnoreCase(r2.getName())).collect(Collectors.toList());

			return ExchangeCurrencyRest.builder().baseCurrency(baseCurrency).day(day)
					.destinationCurrency(orderedDestinationList).build();
		}

	}

	@Override
	public Ratio retriveRatiosByCurrencyAndDay(String currency, LocalDate day) {
		return ratioRepository.findByCurrencyAndDay(currencyRepository.getOne(currency), day);
	}

	@Override
	public float calculateAmount(String baseCurrencyId, String destinationCurrencyId, Float quantity, LocalDate day) {
		if (baseCurrencyId == destinationCurrencyId)
			return quantity;
		final Float ratioExchangeBase = baseCurrencyId.equals(systemBaseCurrencyId) ? SYSTEM_BASE_RATIO_EXCHANGE
				: retriveRatiosByCurrencyAndDay(baseCurrencyId, day).getRatioExchange();
		final Float ratioExchangeDestination = destinationCurrencyId.equals(systemBaseCurrencyId)
				? SYSTEM_BASE_RATIO_EXCHANGE
				: retriveRatiosByCurrencyAndDay(destinationCurrencyId, day).getRatioExchange();
		return ratioExchangeDestination / ratioExchangeBase * quantity;
	}

	private RatioRest defaultRatioRest() {
		return defaultRatioRest(null);
	}

	private RatioRest defaultRatioRest(Long ratioId) {
		final Currency currency = currencyRepository.getOne(systemBaseCurrencyId);
		return RatioRest.builder().id(ratioId).name(currency.getName()).isoCode(currency.getIsoCode())
				.ratio(SYSTEM_BASE_RATIO_EXCHANGE).build();
	}

}
