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

	@ApiModelProperty(value = "The admin's id")
	@JsonProperty
	private Long id;

	@ApiModelProperty(value = "The username's admin")
	@JsonProperty
	private String username;

	@ApiModelProperty(value = "The email's admin")
	@JsonProperty
	private String email;

	@ApiModelProperty(value = "The status of admin")
	@JsonProperty
	private boolean deleted;

}
