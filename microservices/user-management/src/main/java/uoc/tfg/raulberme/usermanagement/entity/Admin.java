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
public class Admin extends User {

	@Column(nullable = false)
	private boolean deleted;

	@Builder
	public Admin(final String username, final String email, final String password, final RolUserType rol) {
		super(username, email, password, RolUserType.ADMIN);
		this.deleted = false;
	}

	@Override
	public boolean canSignin() {
		return !deleted;
	}

}
