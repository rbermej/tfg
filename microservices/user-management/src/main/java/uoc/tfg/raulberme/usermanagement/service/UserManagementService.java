package uoc.tfg.raulberme.usermanagement.service;

import java.util.Collection;

import uoc.tfg.raulberme.usermanagement.dto.AdminDTO;
import uoc.tfg.raulberme.usermanagement.dto.RegisteredUserDTO;
import uoc.tfg.raulberme.usermanagement.entity.RolUserType;
import uoc.tfg.raulberme.usermanagement.form.AdminLoginForm;
import uoc.tfg.raulberme.usermanagement.form.RegisteredUserUpdateForm;
import uoc.tfg.raulberme.usermanagement.form.UserLoginForm;

public interface UserManagementService {

	public Collection<RegisteredUserDTO> listUsers(final String tokenId);

	public Collection<AdminDTO> listAdmins(final String tokenId);

	public void login(final UserLoginForm user);

	public void login(final String tokenId, final AdminLoginForm admin);

	public String signin(final String username, final String password);

	public void signout(final String tokenId);

	public void updatePassword(final String tokenId, final String oldPassword, final String newPassword);

	public void updateUser(final String tokenId, final RegisteredUserUpdateForm userForm);

	public void deletedUser(final String tokenId, final String password);

	public void deletedAdmin(final String tokenId, final Long id);

	public void lockUser(final String tokenId, final Long id);

	public void comproveAuthorization(final String tokenId, final RolUserType rol);

	public String getUsernameByToken(final String tokenId);

	public boolean existsUserByUsername(final String tokenId, final String username);

}
