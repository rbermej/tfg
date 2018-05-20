package uoc.tfg.raulberme.usermanagement.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RegisteredUserUpdateForm {

	@Email(message = "Email should be valid")
	@NotNull
	private String email;

	@NotNull
	private String password;

	@NotNull
	private String defaultCurrency;

}
