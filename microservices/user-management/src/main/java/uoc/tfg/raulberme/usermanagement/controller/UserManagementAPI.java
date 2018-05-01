package uoc.tfg.raulberme.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import uoc.tfg.raulberme.usermanagement.dto.AdminDTO;
import uoc.tfg.raulberme.usermanagement.dto.RegisteredUserDTO;
import uoc.tfg.raulberme.usermanagement.entity.Admin;
import uoc.tfg.raulberme.usermanagement.entity.RegisteredUser;
import uoc.tfg.raulberme.usermanagement.service.UserManagementService;

@RestController
@RequestMapping("/user-management")
@Api(value = "UserManagement", tags = { "UserManagement" })
public class UserManagementAPI {

	private final UserManagementService service;

	@Autowired
	public UserManagementAPI(UserManagementService service) {
		this.service = service;
	}

	public RegisteredUserDTO login(RegisteredUser user) {
		return service.login(user);
	}

	public AdminDTO login(Admin admin) {
		return service.login(admin);
	}

	public Long signin(String username, String password) {
		return service.signin(username, password);
	}

	public void signout(Long id) {
		service.signout(id);
	}

	public void updatePassword(Long id, String oldPassword, String newPassword) {
		service.updatePassword(id, oldPassword, newPassword);
	}

	public void updateUser(Long id, String email, Long currencyId, String password) {
		service.updateUser(id, email, currencyId, password);
	}

	public void deletedUser(Long id, String password) {
		service.deletedUser(id, password);
	}

	public void deletedAdmin(Long id) {
		service.deletedAdmin(id);
	}

	public void lockUser(Long id) {
		service.lockUser(id);
	}

}
