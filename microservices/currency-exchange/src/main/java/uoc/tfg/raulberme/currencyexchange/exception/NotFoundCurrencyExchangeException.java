package uoc.tfg.raulberme.currencyexchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundCurrencyExchangeException extends CurrencyExchangeException {

	private static final long serialVersionUID = 1L;

	public NotFoundCurrencyExchangeException(String message) {
		super(message);
	}

}
