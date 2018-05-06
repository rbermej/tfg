package uoc.tfg.raulberme.currencyexchange.provider;

import uoc.tfg.raulberme.currencyexchange.entity.RolUserType;

public interface UserManagementProvider {

	public void comproveAuthorization(final String tokenId, final RolUserType rol);

}
