package uoc.tfg.raulberme.usermanagement.service;

import java.util.Collection;

import uoc.tfg.raulberme.usermanagement.entity.RolUserType;
import uoc.tfg.raulberme.usermanagement.exception.UserManagementException;
import uoc.tfg.raulberme.usermanagement.form.AdminLoginForm;
import uoc.tfg.raulberme.usermanagement.form.UserLoginForm;

public interface UserManagementService {

	public void login(final UserLoginForm user);

	public void login(final AdminLoginForm admin);

	public Long signin(final String username, final String password) throws UserManagementException;

	public void signout(final Long id);

	public void updatePassword(final Long id, final String oldPassword, final String newPassword);

	public void updateUser(final Long id, final String email, final String currencyId, final String password);

	public void deletedUser(final Long id, final String password);

	public void deletedAdmin(final Long id);

	public void lockUser(final Long id);

	boolean hasAuthorization(final String token, final Collection<RolUserType> roles);

}
