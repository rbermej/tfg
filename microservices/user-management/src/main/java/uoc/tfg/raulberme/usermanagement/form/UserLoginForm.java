package uoc.tfg.raulberme.usermanagement.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserLoginForm {

	@Size(min = 6, max = 30)
	@NotNull
	private String username;

	@Email(message = "Email should be valid")
	@NotNull
	private String email;

	@NotNull
	private String password;

	@NotNull
	private String defaultCurrency;

}
