package uoc.tfg.raulberme.usermanagement.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel(value = "AdminDTO", description = "Complete data of a Rest Admin")
@Data
@Builder
public class AdminDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The currency's ISO Code")
	@JsonProperty
	private String isoCode;

	@ApiModelProperty(value = "The currency's name")
	@JsonProperty
	private String name;

}