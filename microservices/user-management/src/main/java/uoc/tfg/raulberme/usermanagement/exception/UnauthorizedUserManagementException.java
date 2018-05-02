package uoc.tfg.raulberme.usermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedUserManagementException extends UserManagementException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedUserManagementException(String message) {
		super(message);
	}

}
