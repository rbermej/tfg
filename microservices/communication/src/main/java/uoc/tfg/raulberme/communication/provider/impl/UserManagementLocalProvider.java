package uoc.tfg.raulberme.communication.provider.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import uoc.tfg.raulberme.communication.entity.RolUserType;
import uoc.tfg.raulberme.communication.exception.NotFoundCommunicationException;
import uoc.tfg.raulberme.communication.exception.UnauthorizedCommunicationException;
import uoc.tfg.raulberme.communication.provider.UserManagementProvider;

@Component
public class UserManagementLocalProvider implements UserManagementProvider {

	private static final String SYSTEM_TOKEN = "system";
	private static final String ERROR_USER_MANAGEMENT_NOT_AVAILABLE = "ERROR: User Management not available.";
	private static final String ERROR_USER_CANT_BE_AUTHORIZATED = "ERROR: User can't be authorizated.";
	private static final String RESOURCE_URL = "http://localhost:8081/user-management/";

	private final RestTemplate restTemplate;
	private final ObjectMapper mapper;

	@Autowired
	public UserManagementLocalProvider(final RestTemplate restTemplate, final ObjectMapper mapper) {
		this.restTemplate = restTemplate;
		this.mapper = mapper;
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
		try {
			restTemplate.getForEntity(path, Void.class).getBody();
		} catch (final Exception e) {
			throw new UnauthorizedCommunicationException(ERROR_USER_CANT_BE_AUTHORIZATED);
		}
	}

	@Override
	public String retrieveUsernameByToken(final String tokenId) {
		// @formatter:off
		final String path = new StringBuilder(RESOURCE_URL)
								.append("users/")
								.append(tokenId)
								.toString();
		// @formatter:on
		try {
			final ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);
			final String username = mapper.readTree(response.getBody()).path("username").toString();
			return username.substring(1, username.length() - 1);
		} catch (final Exception e) {
			throw new NotFoundCommunicationException(ERROR_USER_MANAGEMENT_NOT_AVAILABLE);
		}
	}

	@Override
	public boolean existsUserByUsername(final String username) {
		// @formatter:off
		final String path = new StringBuilder(RESOURCE_URL)
								.append("users/exists")
								.append("?tokenId=").append(SYSTEM_TOKEN)
								.append("&username=").append(username)
								.toString();
		// @formatter:on		
		return restTemplate.getForEntity(path, Boolean.class).getBody();
	}

}
