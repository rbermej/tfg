package uoc.tfg.raulberme.usermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundUserManagementException extends UserManagementException {

	private static final long serialVersionUID = 1L;

	public NotFoundUserManagementException(String message) {
		super(message);
	}

}
