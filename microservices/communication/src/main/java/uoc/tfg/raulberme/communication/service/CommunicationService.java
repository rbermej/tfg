package uoc.tfg.raulberme.communication.service;

import java.util.Collection;

import uoc.tfg.raulberme.communication.dto.ConversationDTO;
import uoc.tfg.raulberme.communication.dto.MessageDTO;
import uoc.tfg.raulberme.communication.dto.ValuationListsDTO;
import uoc.tfg.raulberme.communication.form.MessageForm;
import uoc.tfg.raulberme.communication.form.ValuationForm;

public interface CommunicationService {

	public ValuationListsDTO listValuationsByEvaluated(final String tokenId, final String evaluated);

	public void evaluate(final String tokenId, final ValuationForm valuation);

	public void sendMessage(final String tokenId, final MessageForm message);

	public Collection<ConversationDTO> listConversationsByParticipant(final String tokenId);

	public Collection<MessageDTO> listMessagesByConversation(final String tokenId, final Long conversationId);

}
