package uoc.tfg.raulberme.currencyexchange.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@ApiModel(value = "PetBaseRest", description = "Complete data of a Rest Pet")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCurrencyDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The base currency")
	@JsonProperty
	private RatioDTO baseCurrency;

	@ApiModelProperty(value = "The ratio's day")
	@JsonProperty
	private LocalDate day;

	@ApiModelProperty(value = "The destination currencies")
	@JsonProperty
	private Collection<RatioDTO> destinationCurrencies;

}
