package uoc.tfg.raulberme.communication.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String author;

	@Column(nullable = false)
	private LocalDateTime date;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Conversation conversation;

	@OneToOne
	@JoinColumn(nullable = false, unique = true)
	private Text text;

	@Builder
	public Message(final String author, final Conversation conversation, final Text text, final LocalDateTime date) {
		super();
		this.author = author;
		this.conversation = conversation;
		this.text = text;
		this.date = date;
	}

}
