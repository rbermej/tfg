package uoc.tfg.raulberme.usermanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.usermanagement.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {

	// public Admin findByUser(final User user);

}
