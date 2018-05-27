package uoc.tfg.raulberme.usermanagement.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import uoc.tfg.raulberme.usermanagement.dto.SigninDTO;
import uoc.tfg.raulberme.usermanagement.entity.RolUserType;
import uoc.tfg.raulberme.usermanagement.form.AdminLoginForm;
import uoc.tfg.raulberme.usermanagement.form.RegisteredUserUpdateForm;
import uoc.tfg.raulberme.usermanagement.form.UserLoginForm;
import uoc.tfg.raulberme.usermanagement.service.UserManagementService;

@RestController
@CrossOrigin
@RequestMapping("/user-management")
@Api(value = "UserManagement", tags = { "UserManagement" })
public class UserManagementAPI {

	private final UserManagementService service;

	@Autowired
	public UserManagementAPI(final UserManagementService service) {
		this.service = service;
	}

	@ApiOperation(value = "Get all RegisteredUsers", notes = "Returns all registered users")
	@GetMapping("/users")
	public @ResponseBody Collection<RegisteredUserDTO> listUsers(@RequestParam final String tokenId) {
		return service.listUsers(tokenId);
	}

	@ApiOperation(value = "Get all Admins", notes = "Returns all admins")
	@GetMapping("/admins")
	public @ResponseBody Collection<AdminDTO> listAdmins(@RequestParam final String tokenId) {
		return service.listAdmins(tokenId);
	}

	@ApiOperation(value = "Login user", notes = "Login user")
	@PutMapping("/users")
	public void login(@Valid @RequestBody final UserLoginForm user) {
		service.login(user);
	}

	@ApiOperation(value = "Login admin", notes = "Login admin")
	@PutMapping("/admins")
	public void login(@RequestParam final String tokenId, @Valid @RequestBody final AdminLoginForm admin) {
		service.login(tokenId, admin);
	}

	@ApiOperation(value = "Signin user", notes = "Signin user (registered user, admin or superadmin)")
	@GetMapping("/signin")
	public @ResponseBody SigninDTO signin(@RequestParam final String username, @RequestParam final String password) {
		return service.signin(username, password);
	}

	@ApiOperation(value = "Signout user", notes = "Signou user (registered user, admin or superadmin)")
	@PostMapping("/signout")
	public void signout(@RequestParam final String tokenId) {
		service.signout(tokenId);
	}

	@ApiOperation(value = "Get RegisteredUser", notes = "Return registered user")
	@GetMapping("/users/{tokenId}")
	public @ResponseBody RegisteredUserDTO getUser(@PathVariable final String tokenId) {
		return service.getUser(tokenId);
	}

	@ApiOperation(value = "Update password", notes = "Update the old password with the new password")
	@PostMapping("/users/{tokenId}/password")
	public void updatePassword(@PathVariable final String tokenId, @RequestParam final String oldPassword,
			@RequestParam final String newPassword) {
		service.updatePassword(tokenId, oldPassword, newPassword);
	}

	@ApiOperation(value = "Update user", notes = "Update the user")
	@PostMapping("/users")
	public void updateUser(@RequestParam final String tokenId, @RequestBody final RegisteredUserUpdateForm userForm) {
		service.updateUser(tokenId, userForm);
	}

	@ApiOperation(value = "Delete user", notes = "Deactivate the user")
	@DeleteMapping("/users/{tokenId}")
	public void deletedUser(@PathVariable final String tokenId, @RequestParam final String password) {
		service.deletedUser(tokenId, password);
	}

	@ApiOperation(value = "Delete admin", notes = "Delete the admin")
	@DeleteMapping("/admins/{id}")
	public void deletedAdmin(@RequestParam final String tokenId, @PathVariable final Long id) {
		service.deletedAdmin(tokenId, id);
	}

	@ApiOperation(value = "Lock user", notes = "Lock the user")
	@PostMapping("/users/{id}/lock")
	public void lockUser(@RequestParam final String tokenId, @PathVariable final Long id) {
		service.lockUser(tokenId, id);
	}

	@ApiOperation(value = "Comprove authorization", notes = "Comprove if user has authorizate")
	@GetMapping("/authorizations")
	public void comproveAuthorization(@RequestParam final String tokenId, @RequestParam final RolUserType rol) {
		service.comproveAuthorization(tokenId, rol);
	}

	@ApiOperation(value = "Exists User by username", notes = "Exists user by username")
	@GetMapping("/users/exists")
	public @ResponseBody boolean existsUserByUsername(@RequestParam final String tokenId,
			@RequestParam final String username) {
		return service.existsUserByUsername(tokenId, username);
	}

}
