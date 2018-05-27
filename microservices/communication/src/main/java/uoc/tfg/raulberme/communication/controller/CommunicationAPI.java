package uoc.tfg.raulberme.communication.controller;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import uoc.tfg.raulberme.communication.dto.ConversationDTO;
import uoc.tfg.raulberme.communication.dto.MessageDTO;
import uoc.tfg.raulberme.communication.dto.ValuationListsDTO;
import uoc.tfg.raulberme.communication.form.MessageForm;
import uoc.tfg.raulberme.communication.form.ValuationForm;
import uoc.tfg.raulberme.communication.service.CommunicationService;

@RestController
@CrossOrigin
@RequestMapping("/communication")
@Api(value = "Communication", tags = { "Communication" })
public class CommunicationAPI {

	private final CommunicationService service;

	@Autowired
	public CommunicationAPI(final CommunicationService service) {
		this.service = service;
	}

	@ApiOperation(value = "", notes = "")
	@GetMapping("valuations/{evaluated}")
	public @ResponseBody ValuationListsDTO listValuationsByEvaluated(@RequestParam final String tokenId,
			@PathVariable final String evaluated) {
		return service.listValuationsByEvaluated(tokenId, evaluated);
	}

	@ApiOperation(value = "", notes = "")
	@PutMapping("valuations")
	public void evaluate(@RequestParam final String tokenId, @Valid @RequestBody final ValuationForm valuation) {
		service.evaluate(tokenId, valuation);
	}

	@ApiOperation(value = "", notes = "")
	@PutMapping("messages")
	public void sendMessage(@RequestParam final String tokenId, @Valid @RequestBody final MessageForm message) {
		service.sendMessage(tokenId, message);
	}

	@ApiOperation(value = "", notes = "")
	@GetMapping("conversations")
	public @ResponseBody Collection<ConversationDTO> listConversationsByParticipant(
			@RequestParam final String tokenId) {
		return service.listConversationsByParticipant(tokenId);
	}

	@ApiOperation(value = "", notes = "")
	@GetMapping("messages")
	public @ResponseBody Collection<MessageDTO> listMessagesByConversation(@RequestParam final String tokenId,
			@RequestParam final Long conversationId) {
		return service.listMessagesByConversation(tokenId, conversationId);
	}

}
