package uoc.tfg.raulberme.usermanagement.service;

import uoc.tfg.raulberme.usermanagement.dto.AdminDTO;
import uoc.tfg.raulberme.usermanagement.dto.RegisteredUserDTO;
import uoc.tfg.raulberme.usermanagement.entity.Admin;
import uoc.tfg.raulberme.usermanagement.entity.RegisteredUser;
import uoc.tfg.raulberme.usermanagement.exception.UserManagementException;

public interface UserManagementService {

	public RegisteredUserDTO login(final RegisteredUser user);

	public AdminDTO login(final Admin admin);

	public Long signin(final String username, final String password) throws UserManagementException;

	public void signout(final Long id);

	public void updatePassword(final Long id, final String oldPassword, final String newPassword);

	public void updateUser(final Long id, final String email, final String currencyId, final String password);

	public void deletedUser(final Long id, final String password);

	public void deletedAdmin(final Long id);

	public void lockUser(final Long id);

}
