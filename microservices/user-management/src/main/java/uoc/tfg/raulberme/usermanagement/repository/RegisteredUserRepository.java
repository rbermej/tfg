package uoc.tfg.raulberme.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.usermanagement.entity.RegisteredUser;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Long> {

	// public RegisteredUser findByUser(final User user);

}
