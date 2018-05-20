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
public class MessageDTO implements Serializable, Comparable<MessageDTO> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The message's author")
	@JsonProperty
	private String author;

	@ApiModelProperty(value = "The message's text")
	@JsonProperty
	private String text;

	@ApiModelProperty(value = "The message's date")
	@JsonProperty
	private LocalDateTime date;

	@Override
	public int compareTo(final MessageDTO other) {
		return this.date.compareTo(other.getDate());
	}

}
