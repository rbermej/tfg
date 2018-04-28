package uoc.tfg.raulberme.currencyexchange.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel(value = "RatioDTO", description = "Complete data of a Rest Ratio")
@Data
@Builder
public class RatioDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The ratio's id", required = false)
	@JsonProperty
	private Long id;

	@ApiModelProperty(value = "The ratio's currency")
	@JsonProperty
	private CurrencyDTO currency;

	@ApiModelProperty(value = "The ratio's value")
	@JsonProperty
	private Float value;

}
