package uoc.tfg.raulberme.communication.provider;

import uoc.tfg.raulberme.communication.entity.RolUserType;

public interface UserManagementProvider {

	public void comproveAuthorization(final String tokenId, final RolUserType rol);

	public String retrieveUsernameByToken(final String tokenId);

	public boolean existsUserByUsername(final String username);

}
