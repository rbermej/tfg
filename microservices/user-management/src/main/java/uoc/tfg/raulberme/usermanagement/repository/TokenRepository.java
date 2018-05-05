package uoc.tfg.raulberme.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.usermanagement.entity.Token;
import uoc.tfg.raulberme.usermanagement.entity.User;

public interface TokenRepository extends JpaRepository<Token, String> {

	public boolean existsByUser(final User user);

}
