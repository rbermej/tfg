package uoc.tfg.raulberme.pruchase.provider.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import uoc.tfg.raulberme.pruchase.entity.RolUserType;
import uoc.tfg.raulberme.pruchase.provider.UserManagementProvider;

@Component
public class UserManagementLocalProvider implements UserManagementProvider {

	private static final String RESOURCE_URL = "http://localhost:8081/user-management/";

	private final RestTemplate restTemplate;

	@Autowired
	public UserManagementLocalProvider(final RestTemplate restTemplate, final ObjectMapper mapper) {
		this.restTemplate = restTemplate;
	}

	@Override
	public void comproveAuthorization(final String tokenId, final RolUserType rol) {
		// @formatter:off
		final String path = new StringBuilder(RESOURCE_URL)
								.append("authorizations")
								.append("?tokenId=").append(tokenId)
								.append("&rol=").append(rol.toString())
								.toString();
		// @formatter:on
		restTemplate.getForEntity(path, Boolean.class).getBody();
	}

	@Override
	public String retrieveUsernameByToken(final String tokenId) {
		// @formatter:off
		final String path = new StringBuilder(RESOURCE_URL)
								.append("users/")
								.append(tokenId)
								.toString();
		// @formatter:on
		return restTemplate.getForEntity(path, String.class).getBody();
	}

}
