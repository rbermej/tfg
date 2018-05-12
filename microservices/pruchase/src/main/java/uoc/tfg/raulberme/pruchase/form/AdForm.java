package uoc.tfg.raulberme.pruchase.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AdForm {

	private Long id;

	@NotNull
	private Float offeredAmount;

	@NotNull
	private String offeredCurrency;

	@NotNull
	private Float demandedAmount;

	@NotNull
	private String demandedCurrency;

	@NotNull
	private String location;

}
