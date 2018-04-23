package uoc.tfg.raulberme.currencyexchange.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

//@ApiModel(value = "PetBaseRest", description = "Complete data of a Rest Pet")
@Data
@Builder
public class CurrencyWithRatioRest implements Serializable {

	private static final long serialVersionUID = 1529769808872008113L;

	// @ApiModelProperty(value = "The pet's id", required = false)
	@JsonProperty
	private Long id;

	@JsonProperty
	private String name;

	@JsonProperty
	private String acronym;

	@JsonProperty
	private Float ratio;

}
