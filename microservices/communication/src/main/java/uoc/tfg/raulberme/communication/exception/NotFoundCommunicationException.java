package uoc.tfg.raulberme.communication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundCommunicationException extends CommunicationException {

	private static final long serialVersionUID = 1L;

	public NotFoundCommunicationException(String message) {
		super(message);
	}

}
