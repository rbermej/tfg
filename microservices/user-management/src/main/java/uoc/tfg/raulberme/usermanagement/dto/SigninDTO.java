package uoc.tfg.raulberme.usermanagement.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import uoc.tfg.raulberme.usermanagement.entity.RolUserType;

@ApiModel(value = "SigninDTO", description = "Complete data of a Signin")
@Data
@Builder
public class SigninDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The token's user")
	@JsonProperty
	private String token;

	@ApiModelProperty(value = "The rol's user")
	@JsonProperty
	private RolUserType rol;

}
