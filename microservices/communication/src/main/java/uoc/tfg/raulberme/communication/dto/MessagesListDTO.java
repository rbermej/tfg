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
public class MessagesListDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The conversation id")
	@JsonProperty
	private Long conversationId;

	@ApiModelProperty(value = "The conversation's messages list")
	@JsonProperty
	private Collection<MessageDTO> messages;

}
