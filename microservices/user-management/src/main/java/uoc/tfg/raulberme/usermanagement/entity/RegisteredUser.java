package uoc.tfg.raulberme.usermanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class RegisteredUser extends User {

	private static final byte MAX_TRIES = 3;

	@Column
	private UserStatusType status;

	@Column
	private byte tries;

	@Column
	private String defaultCurrency;

	@Override
	public boolean canSignin() {
		return status == UserStatusType.ACTIVATED;
	}

	public void initiateTries() {
		tries = MAX_TRIES;
	}

}
