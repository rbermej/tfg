package uoc.tfg.raulberme.communication.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel(value = "ValuationDTO", description = "Complete data of a Rest Valuation")
@Data
@Builder
public class ValuationDTO implements Serializable, Comparable<ValuationDTO> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The valuation's id")
	@JsonProperty
	private Long id;

	@ApiModelProperty(value = "The valuation's points")
	@JsonProperty
	private Integer points;

	@ApiModelProperty(value = "The valuation's date")
	@JsonProperty
	private LocalDateTime date;

	@ApiModelProperty(value = "The valuation's evaluator")
	@JsonProperty
	private String evaluator;

	@ApiModelProperty(value = "The valuation's evaluated")
	@JsonProperty
	private String evaluated;

	@ApiModelProperty(value = "The valuation's text")
	@JsonProperty
	private String text;

	@Override
	public int compareTo(ValuationDTO other) {
		return other.getDate().compareTo(this.getDate());
	}

}
