package uoc.tfg.raulberme.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.usermanagement.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findByUsername(final String username);

}
