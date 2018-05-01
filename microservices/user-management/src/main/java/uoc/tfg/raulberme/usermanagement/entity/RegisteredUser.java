package uoc.tfg.raulberme.usermanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class RegisteredUser extends User {

	@Column
	private UserStatusType status;

	@Column
	private byte tries;

	@Column
	private String defaultCurrency;

}
