package uoc.tfg.raulberme.currencyexchange.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Currency {

	@Id
	private String isoCode;

	@Column(unique = true)
	private String name;

}
