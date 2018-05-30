package uoc.tfg.raulberme.communication.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MessageForm {

	@NotNull
	private Long conversationId;

	@NotNull
	private String text;

}
