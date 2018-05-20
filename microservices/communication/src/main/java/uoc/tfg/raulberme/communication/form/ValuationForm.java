package uoc.tfg.raulberme.communication.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import uoc.tfg.raulberme.communication.entity.UserRolOnAd;

@Data
public class ValuationForm {

	@NotNull
	private String evaluated;

	@NotNull
	private UserRolOnAd rol;

	@Min(1)
	@Max(5)
	@NotNull
	private Integer points;

	private String text;

}
