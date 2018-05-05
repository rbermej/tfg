package uoc.tfg.raulberme.usermanagement.service;

import uoc.tfg.raulberme.usermanagement.entity.RolUserType;
import uoc.tfg.raulberme.usermanagement.exception.UserManagementException;
import uoc.tfg.raulberme.usermanagement.form.AdminLoginForm;
import uoc.tfg.raulberme.usermanagement.form.UserLoginForm;

public interface UserManagementService {

	public void login(final UserLoginForm user) throws UserManagementException;

	public void login(final AdminLoginForm admin);

	public String signin(final String username, final String password) throws UserManagementException;

	public void signout(final String tokenId);

	public void updatePassword(final String tokenId, final String oldPassword, final String newPassword)
			throws UserManagementException;

	public void updateUser(final String tokenId, final String email, final String currencyId, final String password)
			throws UserManagementException;

	public void deletedUser(final String tokenId, final String password) throws UserManagementException;

	public void deletedAdmin(final Long id);

	public void lockUser(final Long id);

	public boolean hasAuthorization(final String tokenId, final RolUserType rol) throws UserManagementException;

}
