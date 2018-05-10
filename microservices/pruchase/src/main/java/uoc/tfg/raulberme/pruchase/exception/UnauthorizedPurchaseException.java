package uoc.tfg.raulberme.pruchase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedPurchaseException extends PurchaseException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedPurchaseException(String message) {
		super(message);
	}

}
