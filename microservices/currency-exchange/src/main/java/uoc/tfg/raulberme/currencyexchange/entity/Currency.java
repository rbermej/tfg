package uoc.tfg.raulberme.currencyexchange.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Currency {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String name;

	@Column(unique = true)
	private String acronym;

	@Column(unique = true)
	private String sign;

}
