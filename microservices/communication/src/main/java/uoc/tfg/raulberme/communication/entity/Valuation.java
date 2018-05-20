package uoc.tfg.raulberme.communication.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Valuation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Integer points;

	@Column(nullable = false)
	private LocalDateTime date;

	@Column(nullable = false)
	private String evaluator;

	@Column(nullable = false)
	private String evaluated;

	@Column(nullable = false)
	private UserRolOnAd rol;

	@OneToOne
	@JoinColumn
	private Text text;

}
