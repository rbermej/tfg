package uoc.tfg.raulberme.currencyexchange.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Ratio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Float ratioExchange;

	@Column(nullable = false)
	private LocalDate day;

	@ManyToOne
	@JoinColumn(name = "currency_id", nullable = false)
	private Currency currency;

}
