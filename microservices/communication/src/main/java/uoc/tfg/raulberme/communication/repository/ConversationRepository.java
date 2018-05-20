package uoc.tfg.raulberme.communication.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import uoc.tfg.raulberme.communication.entity.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

	public Conversation findByUser1AndUser2(final String user1, final String user2);

	public Collection<Conversation> findByUser1OrUser2(final String user1, final String user2);

}
