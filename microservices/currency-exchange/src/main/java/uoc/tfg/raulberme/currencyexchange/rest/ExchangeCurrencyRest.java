package uoc.tfg.raulberme.currencyexchange.rest;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

//@ApiModel(value = "PetBaseRest", description = "Complete data of a Rest Pet")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCurrencyRest implements Serializable {

	private static final long serialVersionUID = 1529769808872008113L;

	// @ApiModelProperty(value = "The pet's id", required = false)
	@JsonProperty
	private CurrencyWithRatioRest baseCurrency;

	@JsonProperty
	private LocalDate day;

	@JsonProperty
	private Collection<CurrencyWithRatioRest> destinationCurrency;

}
