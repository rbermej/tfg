package uoc.tfg.raulberme.currencyexchange.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
import uoc.tfg.raulberme.currencyexchange.dto.CurrencyDTO;
import uoc.tfg.raulberme.currencyexchange.dto.ExchangeCurrencyDTO;
import uoc.tfg.raulberme.currencyexchange.dto.RatioDTO;
import uoc.tfg.raulberme.currencyexchange.entity.Currency;
import uoc.tfg.raulberme.currencyexchange.entity.Ratio;
import uoc.tfg.raulberme.currencyexchange.provider.RatioProvider;
import uoc.tfg.raulberme.currencyexchange.repository.CurrencyRepository;
import uoc.tfg.raulberme.currencyexchange.repository.RatioRepository;
import uoc.tfg.raulberme.currencyexchange.service.CurrencyExchangeService;

@Service
@Log
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

	private static final String RATIO_NOT_FOUND_ACCESSING_EXTERNAL_PROVIDER = "Ratio not found, accessing on external excange currency provider";
	private final String systemBaseCurrencyId;
	private final CurrencyRepository currencyRepository;
	private final RatioRepository ratioRepository;
	private final RatioProvider ratioProvider;

	@Autowired
	public CurrencyExchangeServiceImpl(@Value("${data.default.base_currency_iso}") String systemBaseCurrencyId,
			CurrencyRepository currencyRepository, RatioRepository ratioRepository, RatioProvider ratioProvider) {
		this.systemBaseCurrencyId = systemBaseCurrencyId;
		this.currencyRepository = currencyRepository;
		this.ratioRepository = ratioRepository;
		this.ratioProvider = ratioProvider;
	}

	@Override
	public Collection<CurrencyDTO> listAllCurrencies() {
		return currencyRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public ExchangeCurrencyDTO listRatiosByDay(LocalDate day) {
		return listRatiosByDay(systemBaseCurrencyId, day);
	}

	@Override
	public ExchangeCurrencyDTO listRatiosByDay(String baseCurrencyId, LocalDate day) {

		if (!ratioRepository.existsByDay(day))
			findAndSaveExternalRatios(day);

		RatioDTO baseCurrency = null;
		final Collection<Ratio> listRatios = ratioRepository.findByDay(day);
		final Collection<RatioDTO> destinationCurrency = listRatios.stream().map(this::convertToDto)
				.collect(Collectors.toList());

		if (systemBaseCurrencyId.equals(baseCurrencyId)) {
			baseCurrency = defaultRatioDTO();
		} else {
			destinationCurrency.add(defaultRatioDTO());

			final Optional<RatioDTO> baseRatio = destinationCurrency.stream()
					.filter(r -> baseCurrencyId.equals(r.getCurrency().getIsoCode())).findFirst();
			if (baseRatio.isPresent()) {
				baseCurrency = baseRatio.get();
				final float baseRatioValue = baseCurrency.getValue();
				destinationCurrency.forEach((final RatioDTO r) -> r.setValue(r.getValue() / baseRatioValue));
				destinationCurrency.remove(baseCurrency);
			}
		}

		final Collection<RatioDTO> orderedDestinationList = destinationCurrency.stream()
				.sorted((r1, r2) -> r1.getCurrency().getName().compareToIgnoreCase(r2.getCurrency().getName()))
				.collect(Collectors.toList());

		return ExchangeCurrencyDTO.builder().baseCurrency(baseCurrency).day(day)
				.destinationCurrency(orderedDestinationList).build();

	}

	@Override
	public float calculateAmount(String baseCurrencyId, String destinationCurrencyId, Float quantity, LocalDate day) {
		if (baseCurrencyId.equalsIgnoreCase(destinationCurrencyId))
			return quantity;

		if (!ratioRepository.existsByDay(day))
			findAndSaveExternalRatios(day);

		final Float ratioExchangeBase = baseCurrencyId.equals(systemBaseCurrencyId)
				? retriveDefaultBaseRatio().getRatioExchange()
				: retriveRatioByCurrencyAndDay(baseCurrencyId, day).getRatioExchange();
		final Float ratioExchangeDestination = destinationCurrencyId.equals(systemBaseCurrencyId)
				? retriveDefaultBaseRatio().getRatioExchange()
				: retriveRatioByCurrencyAndDay(destinationCurrencyId, day).getRatioExchange();
		return quantity * ratioExchangeDestination / ratioExchangeBase;
	}

	private void findAndSaveExternalRatios(LocalDate day) {
		log.info(RATIO_NOT_FOUND_ACCESSING_EXTERNAL_PROVIDER);
		Collection<Currency> currencies = currencyRepository.findAll();
		final Map<String, Float> mapExternalRatios = ratioProvider.findByDay(day);
		final Collection<Ratio> listExternalRatios = new ArrayList<>();
		mapExternalRatios.forEach((k, v) -> listExternalRatios
				.add(createRatio(currencies.stream().filter(c -> c.getIsoCode().equals(k)).findFirst().get(), v, day)));
		listExternalRatios.forEach(ratioRepository::save);
	}

	private RatioDTO defaultRatioDTO() {
		return convertToDto(retriveDefaultBaseRatio());
	}

	private Ratio retriveDefaultBaseRatio() {
		return retriveRatioByCurrencyAndDay(systemBaseCurrencyId, null);
	}

	private Ratio retriveRatioByCurrencyAndDay(String currency, LocalDate day) {
		return ratioRepository.findByCurrencyAndDay(currencyRepository.getOne(currency), day);
	}

	private Ratio createRatio(Currency currency, Float value, LocalDate day) {
		// @formatter:off
		return Ratio.builder()
				.currency(currency)
				.ratioExchange(value)
				.day(day)
				.build();
		// @formatter:on
	}

	private RatioDTO convertToDto(Ratio ratio) {
		// @formatter:off
		return RatioDTO.builder()
				.id(ratio.getId())
				.currency(convertToDto(ratio.getCurrency()))
				.value(ratio.getRatioExchange())
				.build();
		// @formatter:on
	}

	private CurrencyDTO convertToDto(Currency currency) {
		// @formatter:off
		return CurrencyDTO.builder()
				.name(currency.getName())
				.isoCode(currency.getIsoCode())
				.build();
		// @formatter:on
	}

}
