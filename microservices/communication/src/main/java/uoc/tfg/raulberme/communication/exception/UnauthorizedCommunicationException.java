package uoc.tfg.raulberme.communication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedCommunicationException extends CommunicationException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedCommunicationException(String message) {
		super(message);
	}

}
