package uoc.tfg.raulberme.communication.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@ApiModel(value = "ConversationDTO", description = "Complete data of a Rest Conversation")
@Data
@Builder
public class ConversationDTO implements Serializable, Comparable<ConversationDTO> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The conversation's id")
	@JsonProperty
	private Long id;

	@ApiModelProperty(value = "The conversation's user1")
	@JsonProperty
	private String user1;

	@ApiModelProperty(value = "The conversation's user2")
	@JsonProperty
	private String user2;

	@ApiModelProperty(value = "The conversation's number of messages")
	@JsonProperty
	private Integer numberMessages;

	@ApiModelProperty(value = "The conversation's date (last message)")
	@JsonProperty
	private LocalDateTime date;

	@Override
	public int compareTo(final ConversationDTO other) {
		return other.getDate().compareTo(this.date);
	}

}
