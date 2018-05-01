package uoc.tfg.raulberme.usermanagement.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uoc.tfg.raulberme.usermanagement.dto.AdminDTO;
import uoc.tfg.raulberme.usermanagement.dto.RegisteredUserDTO;
import uoc.tfg.raulberme.usermanagement.entity.Admin;
import uoc.tfg.raulberme.usermanagement.entity.RegisteredUser;
import uoc.tfg.raulberme.usermanagement.entity.User;
import uoc.tfg.raulberme.usermanagement.entity.UserStatusType;
import uoc.tfg.raulberme.usermanagement.exception.UserManagementException;
import uoc.tfg.raulberme.usermanagement.repository.AdminRepository;
import uoc.tfg.raulberme.usermanagement.repository.RegisteredUserRepository;
import uoc.tfg.raulberme.usermanagement.repository.UserRepository;
import uoc.tfg.raulberme.usermanagement.service.UserManagementService;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	private final UserRepository userRepository;
	private final AdminRepository adminRepository;
	private final RegisteredUserRepository registeredUserRepository;

	@Autowired
	public UserManagementServiceImpl(final UserRepository userRepository, final AdminRepository adminRepository,
			final RegisteredUserRepository registeredUserRepository) {
		this.userRepository = userRepository;
		this.adminRepository = adminRepository;
		this.registeredUserRepository = registeredUserRepository;
	}

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
	public Long signin(final String username, final String password) throws UserManagementException {
		final User user = retrieveUser(username);
		if (user == null) {
			throw new UserManagementException("ERROR: username '" + username + "' not found.");
		}
		if (!user.canSignin()) {
			throw new UserManagementException("ERROR: user can't be signed in.");
		}
		if (!user.getPassword().equals(password)) {
			if (user instanceof RegisteredUser) {
				final RegisteredUser registeredUser = (RegisteredUser) user;
				final byte tries = (byte) (registeredUser.getTries() - 1);
				registeredUser.setTries(tries);
				if (tries == 0) {
					registeredUser.setStatus(UserStatusType.BLOQUED);
					registeredUserRepository.save(registeredUser);
					throw new UserManagementException("ERROR: incorrect password. Your user has been bloqued.");
				}
				registeredUserRepository.save(registeredUser);
				throw new UserManagementException("ERROR: incorrect password. Number of available attempts: " + tries);
			}
			throw new UserManagementException("ERROR: incorrect password.");
		}
		if (user instanceof RegisteredUser) {
			((RegisteredUser) user).initiateTries();
		}
		return user.getId();
	}

	private User retrieveUser(final String username) {
		final User user = userRepository.findByUsername(username);
		if (user == null) {
			return null;
		}
		switch (user.getRol()) {
		case SUPERADMIN:
			return user;
		case ADMIN:
			return adminRepository.getOne(user.getId());
		case REGISTERED_USER:
			return registeredUserRepository.getOne(user.getId());
		default:
			return null;
		}

	}

	@Override
	public void signout(final Long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePassword(final Long id, final String oldPassword, final String newPassword) {
		final User user = userRepository.getOne(id);
		if (oldPassword.equals(user.getPassword())) {
			user.setPassword(newPassword);
			userRepository.save(user);
		}
	}

	@Override
	public void updateUser(final Long id, final String email, final String currencyId, final String password) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deletedUser(final Long id, final String password) {
		final RegisteredUser user = registeredUserRepository.getOne(id);
		if (password.equals(user.getPassword())) {
			user.setStatus(UserStatusType.DEACTIVATED);
			registeredUserRepository.save(user);
		}

	}

	@Override
	public void deletedAdmin(final Long id) {
		final Admin admin = adminRepository.getOne(id);
		admin.setDeleted(true);
		adminRepository.save(admin);
	}

	@Override
	public void lockUser(final Long id) {
		final RegisteredUser user = registeredUserRepository.getOne(id);
		user.setStatus(UserStatusType.BLOQUED);
		registeredUserRepository.save(user);
	}

}
