package uoc.tfg.raulberme.currencyexchange.provider.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import uoc.tfg.raulberme.currencyexchange.entity.RolUserType;
import uoc.tfg.raulberme.currencyexchange.provider.UserManagementProvider;

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
		restTemplate.getForEntity(getPath(tokenId, rol), Boolean.class).getBody();
	}

	private String getPath(final String tokenId, final RolUserType rol) {
		// @formatter:off
		return new StringBuilder(RESOURCE_URL)
				.append("authorizations")
				.append("?tokenId=").append(tokenId)
				.append("&rol=").append(rol.toString())
				.toString();
		// @formatter:on
	}

}
