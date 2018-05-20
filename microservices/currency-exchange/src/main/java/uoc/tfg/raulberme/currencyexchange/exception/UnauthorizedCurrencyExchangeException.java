package uoc.tfg.raulberme.currencyexchange.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedCurrencyExchangeException extends CurrencyExchangeException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedCurrencyExchangeException(String message) {
		super(message);
	}

}
