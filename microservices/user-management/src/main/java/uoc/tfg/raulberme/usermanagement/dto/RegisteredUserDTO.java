package uoc.tfg.raulberme.usermanagement.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import uoc.tfg.raulberme.usermanagement.entity.UserStatusType;

@ApiModel(value = "RegisteredUserDTO", description = "Complete data of a Rest RegisteredUser")
@Data
@Builder
public class RegisteredUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The user's id")
	@JsonProperty
	private Long id;

	@ApiModelProperty(value = "The username's uer")
	@JsonProperty
	private String username;

	@ApiModelProperty(value = "The email's user")
	@JsonProperty
	private String email;

	@ApiModelProperty(value = "The status of user")
	@JsonProperty
	private UserStatusType status;

}
