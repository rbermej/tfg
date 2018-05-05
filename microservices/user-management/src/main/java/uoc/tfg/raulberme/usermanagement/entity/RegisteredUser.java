package uoc.tfg.raulberme.usermanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class RegisteredUser extends User {

	private static final byte MAX_TRIES = 3;

	@Column(nullable = false)
	private UserStatusType status;

	@Column(nullable = false)
	private byte tries;

	@Column(nullable = false)
	private String defaultCurrency;

	@Builder
	public RegisteredUser(final String username, final String email, final String password, final RolUserType rol,
			final String defaultCurrency) {
		super(username, email, password, RolUserType.REGISTERED_USER);
		this.status = UserStatusType.ACTIVATED;
		this.defaultCurrency = defaultCurrency;
		initiateTries();
	}

	@Override
	public boolean canSignin() {
		return status == UserStatusType.ACTIVATED;
	}

	public void initiateTries() {
		tries = MAX_TRIES;
	}

}
