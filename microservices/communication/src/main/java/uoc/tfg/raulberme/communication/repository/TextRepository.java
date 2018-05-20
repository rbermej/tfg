package uoc.tfg.raulberme.communication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.communication.entity.Text;

public interface TextRepository extends JpaRepository<Text, Long> {

}
