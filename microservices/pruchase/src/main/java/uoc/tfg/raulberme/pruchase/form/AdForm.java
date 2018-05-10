package uoc.tfg.raulberme.pruchase.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AdForm {

	private Long id;

	@NotNull
	private Float amount;

	private Float minimumExpectedAmount;

	@NotNull
	private String location;

	@NotNull
	private String offeredCurrency;

	@NotNull
	private String demandedCurrency;

}
