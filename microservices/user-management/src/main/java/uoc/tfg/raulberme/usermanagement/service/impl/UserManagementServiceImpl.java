package uoc.tfg.raulberme.usermanagement.service.impl;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.bytebuddy.utility.RandomString;
import uoc.tfg.raulberme.usermanagement.dto.AdminDTO;
import uoc.tfg.raulberme.usermanagement.dto.RegisteredUserDTO;
import uoc.tfg.raulberme.usermanagement.dto.SigninDTO;
import uoc.tfg.raulberme.usermanagement.entity.Admin;
import uoc.tfg.raulberme.usermanagement.entity.RegisteredUser;
import uoc.tfg.raulberme.usermanagement.entity.RolUserType;
import uoc.tfg.raulberme.usermanagement.entity.Token;
import uoc.tfg.raulberme.usermanagement.entity.User;
import uoc.tfg.raulberme.usermanagement.entity.UserStatusType;
import uoc.tfg.raulberme.usermanagement.exception.NotFoundUserManagementException;
import uoc.tfg.raulberme.usermanagement.exception.UnauthorizedUserManagementException;
import uoc.tfg.raulberme.usermanagement.form.AdminLoginForm;
import uoc.tfg.raulberme.usermanagement.form.RegisteredUserUpdateForm;
import uoc.tfg.raulberme.usermanagement.form.UserLoginForm;
import uoc.tfg.raulberme.usermanagement.provider.CurrencyExchangeProvider;
import uoc.tfg.raulberme.usermanagement.repository.AdminRepository;
import uoc.tfg.raulberme.usermanagement.repository.RegisteredUserRepository;
import uoc.tfg.raulberme.usermanagement.repository.TokenRepository;
import uoc.tfg.raulberme.usermanagement.repository.UserRepository;
import uoc.tfg.raulberme.usermanagement.service.UserManagementService;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	private static final String ERROR_USER_CAN_T_BE_SIGNED_IN = "ERROR: user can't be signed in.";
	private static final String ERROR_USER_CANT_BE_AUTHORIZATE = "ERROR: user can't be authorizate.";
	private static final String ERROR_USER_NOT_FOUND = "ERROR: user not found.";
	private static final String ERROR_ADMIN_NOT_FOUND = "ERROR: admin not found.";
	private static final String ERROR_INCORRECT_PASSWORD = "ERROR: incorrect password.";
	private static final String ERROR_TOKEN_NOT_FOUND = "ERROR: token not found.";
	private static final String ERROR_DUPLICATED_EMAIL = "ERROR: duplicated email.";
	private static final String ERROR_DUPLICATED_USERNAME = "ERROR: duplicated username.";
	private static final int LENGTH_TOKEN = 50;

	private final UserRepository userRepository;
	private final AdminRepository adminRepository;
	private final RegisteredUserRepository registeredUserRepository;
	private final TokenRepository tokenRepository;
	private final CurrencyExchangeProvider currencyExchangeProvider;

	@Autowired
	public UserManagementServiceImpl(final UserRepository userRepository, final AdminRepository adminRepository,
			final RegisteredUserRepository registeredUserRepository, final TokenRepository tokenRepository,
			final CurrencyExchangeProvider currencyExchangeProvider) {
		this.userRepository = userRepository;
		this.adminRepository = adminRepository;
		this.registeredUserRepository = registeredUserRepository;
		this.tokenRepository = tokenRepository;
		this.currencyExchangeProvider = currencyExchangeProvider;
	}

	@Override
	public Collection<RegisteredUserDTO> listUsers(final String tokenId) {
		comproveAuthorization(tokenId, RolUserType.ADMIN);
		return registeredUserRepository.findAll().stream().map(this::convertToDTO).sorted()
				.collect(Collectors.toList());
	}

	@Override
	public Collection<AdminDTO> listAdmins(final String tokenId) {
		comproveAuthorization(tokenId, RolUserType.SUPERADMIN);
		return adminRepository.findAll().stream().map(this::convertToDTO).sorted().collect(Collectors.toList());
	}

	@Override
	public void login(final UserLoginForm user) {
		if (userRepository.existsByUsername(user.getUsername()))
			throw new UnauthorizedUserManagementException(ERROR_DUPLICATED_USERNAME);

		if (userRepository.existsByEmail(user.getEmail()))
			throw new UnauthorizedUserManagementException(ERROR_DUPLICATED_EMAIL);

		if (!currencyExchangeProvider.existsCurrency(user.getDefaultCurrency()))
			user.setDefaultCurrency(null);

		registeredUserRepository.save(convertToEntity(user));
	}

	@Override
	public void login(final String tokenId, final AdminLoginForm admin) {
		comproveAuthorization(tokenId, RolUserType.SUPERADMIN);
		if (userRepository.existsByUsername(admin.getUsername()))
			throw new UnauthorizedUserManagementException(ERROR_DUPLICATED_USERNAME);

		if (userRepository.existsByEmail(admin.getEmail()))
			throw new UnauthorizedUserManagementException(ERROR_DUPLICATED_EMAIL);

		adminRepository.save(convertToEntity(admin));
	}

	@Override
	public SigninDTO signin(final String username, final String password) {

		final User user = retrieveFullUser(userRepository.findByUsername(username));

		if (user == null) {
			throw new NotFoundUserManagementException("ERROR: user with username '" + username + "' not found.");
		}

		if (tokenRepository.existsByUser(user)) {
			return convertToDTO(tokenRepository.findByUser(user), user);
		}

		if (!user.canSignin()) {
			throw new UnauthorizedUserManagementException(ERROR_USER_CAN_T_BE_SIGNED_IN);
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
							ERROR_INCORRECT_PASSWORD + " Your user has been bloqued.");
				}
				registeredUserRepository.save(registeredUser);
				throw new UnauthorizedUserManagementException(
						ERROR_INCORRECT_PASSWORD + " Number of available attempts: " + tries + ".");
			}
			throw new UnauthorizedUserManagementException(ERROR_INCORRECT_PASSWORD);
		}

		if (user instanceof RegisteredUser) {
			((RegisteredUser) user).initiateTries();
		}

		final String code = generateTokenCode();
		final Token token = createToken(code, user);
		tokenRepository.save(token);
		return convertToDTO(token, user);
	}

	@Override
	public void signout(final String tokenId) {
		if (!tokenRepository.existsById(tokenId))
			throw new UnauthorizedUserManagementException(ERROR_TOKEN_NOT_FOUND);
		tokenRepository.deleteById(tokenId);
	}

	@Override
	public RegisteredUserDTO getUser(final String tokenId) {
		comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		return convertToDTO((RegisteredUser) retrieveUserByToken(tokenId));
	}

	@Override
	public void updatePassword(final String tokenId, final String oldPassword, final String newPassword) {
		final User user = retrieveUserByToken(tokenId);
		if (!oldPassword.equals(user.getPassword())) {
			throw new UnauthorizedUserManagementException(ERROR_INCORRECT_PASSWORD);
		}
		user.setPassword(newPassword);
		userRepository.save(user);
	}

	@Override
	public void updateUser(final String tokenId, final RegisteredUserUpdateForm userForm) {
		comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final RegisteredUser user = registeredUserRepository.getOne(retrieveUserByToken(tokenId).getId());
		if (!user.getPassword().equals(userForm.getPassword())) {
			throw new UnauthorizedUserManagementException(ERROR_INCORRECT_PASSWORD);
		}
		if (!user.getEmail().equals(userForm.getEmail()) && userRepository.existsByEmail(userForm.getEmail())) {
			throw new UnauthorizedUserManagementException(ERROR_DUPLICATED_EMAIL);
		}
		user.setEmail(userForm.getEmail());
		user.setDefaultCurrency(userForm.getDefaultCurrency());
		registeredUserRepository.save(user);
	}

	@Override
	public void deletedUser(final String tokenId, final String password) {
		comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final RegisteredUser user = registeredUserRepository.getOne(retrieveUserByToken(tokenId).getId());
		if (!password.equals(user.getPassword())) {
			throw new UnauthorizedUserManagementException(ERROR_INCORRECT_PASSWORD);
		}
		user.setStatus(UserStatusType.DEACTIVATED);
		registeredUserRepository.save(user);
	}

	@Override
	public void deletedAdmin(final String tokenId, final Long id) {
		comproveAuthorization(tokenId, RolUserType.SUPERADMIN);
		if (!adminRepository.existsById(id))
			throw new NotFoundUserManagementException(ERROR_ADMIN_NOT_FOUND);
		final Admin admin = adminRepository.getOne(id);
		admin.setDeleted(true);
		adminRepository.save(admin);
	}

	@Override
	public void lockUser(final String tokenId, final Long id) {
		comproveAuthorization(tokenId, RolUserType.ADMIN);
		if (!registeredUserRepository.existsById(id))
			throw new NotFoundUserManagementException(ERROR_USER_NOT_FOUND);
		final RegisteredUser user = registeredUserRepository.getOne(id);
		user.setStatus(UserStatusType.BLOQUED);
		registeredUserRepository.save(user);
	}

	@Override
	public void comproveAuthorization(final String tokenId, final RolUserType rol) {
		if (rol != retrieveUserByToken(tokenId).getRol())
			throw new UnauthorizedUserManagementException(ERROR_USER_CANT_BE_AUTHORIZATE);
	}

	@Override
	public boolean existsUserByUsername(final String tokenId, final String username) {
		comproveAuthorization(tokenId, RolUserType.SYSTEM);
		final RegisteredUser registeredUser = registeredUserRepository.findByUsername(username);
		return registeredUser != null && registeredUser.getStatus() == UserStatusType.ACTIVATED;
	}

	private String generateTokenCode() {
		String code;
		do {
			code = RandomString.make(LENGTH_TOKEN);
		} while (tokenRepository.existsById(code));
		return code;
	}

	private User retrieveUserByToken(final String tokenId) {
		final Optional<Token> token = tokenRepository.findById(tokenId);
		if (!token.isPresent())
			throw new UnauthorizedUserManagementException(ERROR_TOKEN_NOT_FOUND);
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

	private RegisteredUserDTO convertToDTO(final RegisteredUser user) {
		// @formatter:off
		return RegisteredUserDTO.builder()
				.id(user.getId())
				.username(user.getUsername())
				.email(user.getEmail())
				.defaultCurrency(user.getDefaultCurrency())
				.status(user.getStatus())
				.build();
		// @formatter:on
	}

	private AdminDTO convertToDTO(final Admin admin) {
		// @formatter:off
		return AdminDTO.builder()
				.id(admin.getId())
				.username(admin.getUsername())
				.email(admin.getEmail())
				.deleted(admin.isDeleted())
				.build();
		// @formatter:on
	}

	private SigninDTO convertToDTO(final Token token, final User user) {
		return SigninDTO.builder().token(token.getCode()).rol(user.getRol()).build();
	}

}
