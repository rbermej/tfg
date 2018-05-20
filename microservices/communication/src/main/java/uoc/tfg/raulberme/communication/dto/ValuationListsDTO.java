package uoc.tfg.raulberme.communication.dto;

import java.io.Serializable;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel(value = "ValuationDTO", description = "Complete data of a Rest Valuation")
@Data
@Builder
public class ValuationListsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The valuation's list as seller")
	@JsonProperty
	private Collection<ValuationDTO> valutionsAsSeller;

	@ApiModelProperty(value = "The valuation's list as buyer")
	@JsonProperty
	private Collection<ValuationDTO> valutionsAsBuyer;

}
