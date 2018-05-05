package uoc.tfg.raulberme.usermanagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import uoc.tfg.raulberme.usermanagement.entity.RegisteredUser;
import uoc.tfg.raulberme.usermanagement.entity.RolUserType;
import uoc.tfg.raulberme.usermanagement.exception.UserManagementException;
import uoc.tfg.raulberme.usermanagement.form.AdminLoginForm;
import uoc.tfg.raulberme.usermanagement.form.UserLoginForm;
import uoc.tfg.raulberme.usermanagement.service.UserManagementService;

@RestController
@RequestMapping("/user-management")
@Api(value = "UserManagement", tags = { "UserManagement" })
public class UserManagementAPI {

	private final UserManagementService service;

	@Autowired
	public UserManagementAPI(final UserManagementService service) {
		this.service = service;
	}

	@ApiOperation(value = "Login user", notes = "Login user")
	@PutMapping("/user")
	public void login(@Valid @RequestBody final UserLoginForm user) throws UserManagementException {
		service.login(user);
	}

	@ApiOperation(value = "Login admin", notes = "Login admin")
	@PutMapping("/admin")
	public void login(@Valid @RequestBody final AdminLoginForm admin) {
		service.login(admin);
	}

	@ApiOperation(value = "Signin user", notes = "Signin user (registered user, admin or superadmin)")
	@GetMapping("/signin")
	public String signin(@RequestParam final String username, @RequestParam final String password)
			throws UserManagementException {
		return service.signin(username, password);
	}

	public void signout(String tokenId) {
		service.signout(tokenId);
	}

	@ApiOperation(value = "Update password", notes = "Update the old password with the new password")
	@PostMapping("/password")
	public void updatePassword(@RequestParam final String tokenId, @RequestParam final String oldPassword,
			@RequestParam final String newPassword) throws UserManagementException {
		service.updatePassword(tokenId, oldPassword, newPassword);
	}

	@ApiOperation(value = "Update user", notes = "Update the user")
	@PostMapping("/user")
	public void updateUser(final String tokenId, @RequestBody final RegisteredUser user)
			throws UserManagementException {
		service.updateUser(tokenId, user.getEmail(), user.getDefaultCurrency(), user.getPassword());
	}

	@ApiOperation(value = "Delete user", notes = "Deactivate the user")
	@DeleteMapping("/user")
	public void deletedUser(@RequestParam final String tokenId, @RequestParam final String password)
			throws UserManagementException {
		service.deletedUser(tokenId, password);
	}

	@ApiOperation(value = "Delete admin", notes = "Delete the admin")
	@DeleteMapping("/admin")
	public void deletedAdmin(@RequestParam final Long id) {
		service.deletedAdmin(id);
	}

	@ApiOperation(value = "Lock user", notes = "Lock the user")
	@PostMapping("/user/lock")
	public void lockUser(@RequestParam final Long id) {
		service.lockUser(id);
	}

	@ApiOperation(value = "Comprove authorization", notes = "Comprove if user has authorizate")
	@GetMapping("/authorization")
	public boolean hasAuthorization(@RequestParam final String tokenId, @RequestParam final RolUserType rol)
			throws UserManagementException {
		return service.hasAuthorization(tokenId, rol);
	}

}
