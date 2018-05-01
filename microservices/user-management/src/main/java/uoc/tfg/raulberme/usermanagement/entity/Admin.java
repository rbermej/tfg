package uoc.tfg.raulberme.usermanagement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Admin extends User {

	@Column
	private boolean deleted;

	@Override
	public boolean canSignin() {
		return !deleted;
	}

}
