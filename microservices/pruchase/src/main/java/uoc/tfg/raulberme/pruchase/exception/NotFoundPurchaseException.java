package uoc.tfg.raulberme.pruchase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundPurchaseException extends PurchaseException {

	private static final long serialVersionUID = 1L;

	public NotFoundPurchaseException(String message) {
		super(message);
	}

}
