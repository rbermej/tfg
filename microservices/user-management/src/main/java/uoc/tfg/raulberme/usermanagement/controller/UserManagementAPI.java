package uoc.tfg.raulberme.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import uoc.tfg.raulberme.usermanagement.dto.AdminDTO;
import uoc.tfg.raulberme.usermanagement.dto.RegisteredUserDTO;
import uoc.tfg.raulberme.usermanagement.entity.Admin;
import uoc.tfg.raulberme.usermanagement.entity.RegisteredUser;
import uoc.tfg.raulberme.usermanagement.exception.UserManagementException;
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
	@ResponseBody
	public RegisteredUserDTO login(@RequestBody final RegisteredUser user) {
		return service.login(user);
	}

	@ApiOperation(value = "Login admin", notes = "Login admin")
	@PutMapping("/admin")
	@ResponseBody
	public AdminDTO login(@RequestBody final Admin admin) {
		return service.login(admin);
	}

	@ApiOperation(value = "Signin user", notes = "Signin user (registered user, admin or superadmin)")
	@GetMapping("/signin")
	public Long signin(@RequestParam final String username, @RequestParam final String password)
			throws UserManagementException {
		return service.signin(username, password);
	}

	// TODO
	public void signout(Long id) {
		service.signout(id);
	}

	@ApiOperation(value = "Update password", notes = "Update the old password with the new password")
	@PostMapping("/password")
	public void updatePassword(@RequestParam final Long id, @RequestParam final String oldPassword,
			@RequestParam final String newPassword) {
		service.updatePassword(id, oldPassword, newPassword);
	}

	@ApiOperation(value = "Update user", notes = "Update the user")
	@PostMapping("/user")
	public void updateUser(@RequestBody final RegisteredUser user) {
		service.updateUser(user.getId(), user.getEmail(), user.getDefaultCurrency(), user.getPassword());
	}

	@ApiOperation(value = "Delete user", notes = "Deactivate the user")
	@DeleteMapping("/user")
	public void deletedUser(@RequestParam final Long id, @RequestParam final String password) {
		service.deletedUser(id, password);
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

}
