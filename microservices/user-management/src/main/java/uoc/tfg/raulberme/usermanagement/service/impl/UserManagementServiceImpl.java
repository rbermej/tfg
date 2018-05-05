package uoc.tfg.raulberme.usermanagement.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;
import uoc.tfg.raulberme.usermanagement.entity.Admin;
import uoc.tfg.raulberme.usermanagement.entity.RegisteredUser;
import uoc.tfg.raulberme.usermanagement.entity.RolUserType;
import uoc.tfg.raulberme.usermanagement.entity.Token;
import uoc.tfg.raulberme.usermanagement.entity.User;
import uoc.tfg.raulberme.usermanagement.entity.UserStatusType;
import uoc.tfg.raulberme.usermanagement.exception.EntityNotFoundUserManagementException;
import uoc.tfg.raulberme.usermanagement.exception.UnauthorizedUserManagementException;
import uoc.tfg.raulberme.usermanagement.exception.UserManagementException;
import uoc.tfg.raulberme.usermanagement.form.AdminLoginForm;
import uoc.tfg.raulberme.usermanagement.form.UserLoginForm;
import uoc.tfg.raulberme.usermanagement.repository.AdminRepository;
import uoc.tfg.raulberme.usermanagement.repository.RegisteredUserRepository;
import uoc.tfg.raulberme.usermanagement.repository.TokenRepository;
import uoc.tfg.raulberme.usermanagement.repository.UserRepository;
import uoc.tfg.raulberme.usermanagement.service.UserManagementService;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	private static final int LENGTH_TOKEN = 50;
	private final UserRepository userRepository;
	private final AdminRepository adminRepository;
	private final RegisteredUserRepository registeredUserRepository;
	private final TokenRepository tokenRepository;

	@Autowired
	public UserManagementServiceImpl(final UserRepository userRepository, final AdminRepository adminRepository,
			final RegisteredUserRepository registeredUserRepository, final TokenRepository tokenRepository) {
		this.userRepository = userRepository;
		this.adminRepository = adminRepository;
		this.registeredUserRepository = registeredUserRepository;
		this.tokenRepository = tokenRepository;
	}

	@Override
	public void login(final UserLoginForm user) throws UserManagementException {
		if (userRepository.existsByUsername(user.getUsername()))
			throw new UnauthorizedUserManagementException("ERROR: duplicated username.");

		if (userRepository.existsByEmail(user.getEmail()))
			throw new UnauthorizedUserManagementException("ERROR: duplicated email.");

		registeredUserRepository.save(convertToEntity(user));
	}

	@Override
	public void login(final AdminLoginForm admin) {
		adminRepository.save(convertToEntity(admin));
	}

	@Override
	public String signin(final String username, final String password) throws UserManagementException {

		final User user = retrieveFullUser(userRepository.findByUsername(username));

		if (user == null) {
			throw new EntityNotFoundUserManagementException("ERROR: user with username '" + username + "' not found.");
		}

		if (tokenRepository.existsByUser(user)) {
			throw new UnauthorizedUserManagementException("ERROR: user already authorizate.");
		}

		if (!user.canSignin()) {
			throw new UnauthorizedUserManagementException("ERROR: user can't be signed in.");
		}

		if (!user.getPassword().equals(password)) {
			if (user instanceof RegisteredUser) {
				final RegisteredUser registeredUser = (RegisteredUser) user;
				final byte tries = (byte) (registeredUser.getTries() - 1);
				registeredUser.setTries(tries);
				if (tries == 0) {
					registeredUser.setStatus(UserStatusType.BLOQUED);
					registeredUserRepository.save(registeredUser);
					throw new UnauthorizedUserManagementException(
							"ERROR: incorrect password. Your user has been bloqued.");
				}
				registeredUserRepository.save(registeredUser);
				throw new UnauthorizedUserManagementException(
						"ERROR: incorrect password. Number of available attempts: " + tries);
			}
			throw new UnauthorizedUserManagementException("ERROR: incorrect password.");
		}

		if (user instanceof RegisteredUser) {
			((RegisteredUser) user).initiateTries();
		}

		String code = null;
		do {
			code = RandomString.make(LENGTH_TOKEN);
		} while (tokenRepository.existsById(code));

		final Token token = createToken(code, user);
		tokenRepository.save(token);
		return token.getCode();
	}

	@Override
	public void signout(final String tokenId) {
		tokenRepository.deleteById(tokenId);
	}

	@Override
	public void updatePassword(final String tokenId, final String oldPassword, final String newPassword)
			throws UserManagementException {
		final User user = retrieveUserByToken(tokenId);
		if (oldPassword.equals(user.getPassword())) {
			user.setPassword(newPassword);
			userRepository.save(user);
		}
	}

	@Override
	public void updateUser(final String tokenId, final String email, final String currencyId, final String password)
			throws UserManagementException {
		final RegisteredUser user = registeredUserRepository.getOne(retrieveUserByToken(tokenId).getId());
		user.setEmail(email);
		user.setDefaultCurrency(currencyId);
		user.setPassword(password);
		registeredUserRepository.save(user);
	}

	@Override
	public void deletedUser(final String tokenId, final String password) throws UserManagementException {
		final RegisteredUser user = registeredUserRepository.getOne(retrieveUserByToken(tokenId).getId());
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

	@Override
	public boolean hasAuthorization(final String tokenId, final RolUserType rol) throws UserManagementException {
		// return (!rol.isPresent()||rol.get()==retrieveUserByToken(tokenId).getRol());
		return rol == retrieveUserByToken(tokenId).getRol();
	}

	private User retrieveUserByToken(final String tokenId) throws UserManagementException {
		final Optional<Token> token = tokenRepository.findById(tokenId);
		if (!token.isPresent())
			throw new UnauthorizedUserManagementException("ERROR: user can't be authorizate.");
		return token.get().getUser();
	}

	private User retrieveFullUser(final User user) {
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

	private Token createToken(final String code, final User user) {
		// @formatter:off
		return Token.builder()
				.code(code)
				.user(user)
				.build();
		// @formatter:on
	}

	private RegisteredUser convertToEntity(final UserLoginForm user) {
		// @formatter:off
		return RegisteredUser.builder()
				.username(user.getUsername())
				.email(user.getEmail())
				.password(user.getPassword())
				.defaultCurrency(user.getDefaultCurrency())
				.build();
		// @formatter:on
	}

	private Admin convertToEntity(final AdminLoginForm admin) {
		// @formatter:off
		return Admin.builder()
				.username(admin.getUsername())
				.email(admin.getEmail())
				.password(admin.getPassword())
				.build();
		// @formatter:on
	}

}
