package uoc.tfg.raulberme.communication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.communication.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
