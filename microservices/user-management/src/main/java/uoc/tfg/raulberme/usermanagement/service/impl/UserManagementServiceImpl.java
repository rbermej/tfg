package uoc.tfg.raulberme.usermanagement.service.impl;

import org.springframework.stereotype.Service;

import uoc.tfg.raulberme.usermanagement.dto.AdminDTO;
import uoc.tfg.raulberme.usermanagement.dto.RegisteredUserDTO;
import uoc.tfg.raulberme.usermanagement.entity.Admin;
import uoc.tfg.raulberme.usermanagement.entity.RegisteredUser;
import uoc.tfg.raulberme.usermanagement.service.UserManagementService;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	@Override
	public RegisteredUserDTO login(final RegisteredUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminDTO login(final Admin admin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long signin(final String username, final String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void signout(final Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePassword(final Long id, final String oldPassword, final String newPassword) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUser(final Long id, final String email, final Long currencyId, final String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletedUser(final Long id, final String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletedAdmin(final Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void lockUser(final Long id) {
		// TODO Auto-generated method stub

	}

}
