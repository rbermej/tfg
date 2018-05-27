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
import uoc.tfg.raulberme.currencyexchange.entity.RolUserType;
import uoc.tfg.raulberme.currencyexchange.exception.NotFoundCurrencyExchangeException;
import uoc.tfg.raulberme.currencyexchange.provider.RatioProvider;
import uoc.tfg.raulberme.currencyexchange.provider.UserManagementProvider;
import uoc.tfg.raulberme.currencyexchange.repository.CurrencyRepository;
import uoc.tfg.raulberme.currencyexchange.repository.RatioRepository;
import uoc.tfg.raulberme.currencyexchange.service.CurrencyExchangeService;

@Service
@Log
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

	private static final String ERROR_CURRENCY_NOT_FOUND = "ERROR: Currency not found.";
	private static final String RATIO_NOT_FOUND_ACCESSING_EXTERNAL_PROVIDER = "Ratio not found, accessing on external excange currency provider.";

	private final String systemBaseCurrencyId;
	private final CurrencyRepository currencyRepository;
	private final RatioRepository ratioRepository;
	private final RatioProvider ratioProvider;
	private final UserManagementProvider userManagementProvider;

	@Autowired
	public CurrencyExchangeServiceImpl(@Value("${data.default.base_currency_iso}") final String systemBaseCurrencyId,
			final CurrencyRepository currencyRepository, final RatioRepository ratioRepository,
			final RatioProvider ratioProvider, final UserManagementProvider userManagementProvider) {
		this.systemBaseCurrencyId = systemBaseCurrencyId;
		this.currencyRepository = currencyRepository;
		this.ratioRepository = ratioRepository;
		this.ratioProvider = ratioProvider;
		this.userManagementProvider = userManagementProvider;
	}

	@Override
	public Collection<CurrencyDTO> listAllCurrencies() {
		return currencyRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@Override
	public boolean existsCurrency(final String currency) {
		return currencyRepository.existsById(currency);
	}

	@Override
	public ExchangeCurrencyDTO listRatiosByDay(final String tokenId, final LocalDate day) {
		return listRatiosByDay(tokenId, systemBaseCurrencyId, day);
	}

	@Override
	public ExchangeCurrencyDTO listRatiosByDay(final String tokenId, final String baseCurrencyId, final LocalDate day) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);

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
				.destinationCurrencies(orderedDestinationList).build();

	}

	@Override
	public float calculateAmount(final String baseCurrencyId, final String destinationCurrencyId, final Float quantity,
			final LocalDate day) {

		if (!currencyRepository.existsById(baseCurrencyId) || !currencyRepository.existsById(destinationCurrencyId))
			throw new NotFoundCurrencyExchangeException(ERROR_CURRENCY_NOT_FOUND);

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

	private void findAndSaveExternalRatios(final LocalDate day) {
		log.info(RATIO_NOT_FOUND_ACCESSING_EXTERNAL_PROVIDER);
		final Collection<Currency> currencies = currencyRepository.findAll();
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

	private Ratio retriveRatioByCurrencyAndDay(final String currency, final LocalDate day) {
		return ratioRepository.findByCurrencyAndDay(currencyRepository.getOne(currency), day);
	}

	private Ratio createRatio(final Currency currency, final Float value, final LocalDate day) {
		// @formatter:off
		return Ratio.builder()
				.currency(currency)
				.ratioExchange(value)
				.day(day)
				.build();
		// @formatter:on
	}

	private RatioDTO convertToDto(final Ratio ratio) {
		// @formatter:off
		return RatioDTO.builder()
				.id(ratio.getId())
				.currency(convertToDto(ratio.getCurrency()))
				.value(ratio.getRatioExchange())
				.build();
		// @formatter:on
	}

	private CurrencyDTO convertToDto(final Currency currency) {
		// @formatter:off
		return CurrencyDTO.builder()
				.name(currency.getName())
				.isoCode(currency.getIsoCode())
				.build();
		// @formatter:on
	}

}
