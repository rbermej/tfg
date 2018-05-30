package uoc.tfg.raulberme.communication.service.impl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uoc.tfg.raulberme.communication.dto.ConversationDTO;
import uoc.tfg.raulberme.communication.dto.MessageDTO;
import uoc.tfg.raulberme.communication.dto.MessagesListDTO;
import uoc.tfg.raulberme.communication.dto.ValuationDTO;
import uoc.tfg.raulberme.communication.dto.ValuationListsDTO;
import uoc.tfg.raulberme.communication.entity.Conversation;
import uoc.tfg.raulberme.communication.entity.Message;
import uoc.tfg.raulberme.communication.entity.RolUserType;
import uoc.tfg.raulberme.communication.entity.Text;
import uoc.tfg.raulberme.communication.entity.UserRolOnAd;
import uoc.tfg.raulberme.communication.entity.Valuation;
import uoc.tfg.raulberme.communication.exception.NotFoundCommunicationException;
import uoc.tfg.raulberme.communication.exception.UnauthorizedCommunicationException;
import uoc.tfg.raulberme.communication.form.MessageForm;
import uoc.tfg.raulberme.communication.form.ValuationForm;
import uoc.tfg.raulberme.communication.provider.UserManagementProvider;
import uoc.tfg.raulberme.communication.repository.ConversationRepository;
import uoc.tfg.raulberme.communication.repository.MessageRepository;
import uoc.tfg.raulberme.communication.repository.TextRepository;
import uoc.tfg.raulberme.communication.repository.ValuationRepository;
import uoc.tfg.raulberme.communication.service.CommunicationService;

@Service
public class CommunicationServiceImpl implements CommunicationService {

	private static final String ERROR_YOU_ARENT_PARTICIPANT = "ERROR: You aren't participant of this conversation.";
	private static final String ERROR_CONVERSATION_NOT_FOUND = "ERROR: conversation not found.";
	private static final String ERROR_RECEIVER_CANT_BE_SAME_TRANSMITTER = "ERROR: receiver can't be same transmitter.";
	private static final String ERROR_EVALUATED_CANT_BE_SAME_EVALUATOR = "ERROR: evaluated can't be same evaluator.";
	private static final String ERROR_RECIEVER_NOT_FOUND = "ERROR: reciever not found.";
	private static final String ERROR_EVALUATED_USER_NOT_FOUND = "ERROR: evaluated user not found.";

	private final ValuationRepository valuationRepository;
	private final MessageRepository messageRepository;
	private final TextRepository textRepository;
	private final ConversationRepository conversationRepository;
	private final UserManagementProvider userManagementProvider;

	@Autowired
	public CommunicationServiceImpl(final ValuationRepository valuationRepository,
			final MessageRepository messageRepository, final TextRepository textRepository,
			final ConversationRepository conversationRepository, final UserManagementProvider userManagementProvider) {
		super();
		this.valuationRepository = valuationRepository;
		this.messageRepository = messageRepository;
		this.textRepository = textRepository;
		this.conversationRepository = conversationRepository;
		this.userManagementProvider = userManagementProvider;
	}

	@Override
	public ValuationListsDTO listValuationsByEvaluated(final String tokenId, final String evaluated) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		if (!userManagementProvider.existsUserByUsername(evaluated))
			throw new NotFoundCommunicationException(ERROR_EVALUATED_USER_NOT_FOUND);
		final Collection<Valuation> listValuations = valuationRepository.findByEvaluated(evaluated);
		return createValuationListsDTO(
				listValuations.stream().filter(v -> v.getRol() == UserRolOnAd.SELLER).map(this::convertToDTO).sorted()
						.collect(Collectors.toList()),
				listValuations.stream().filter(v -> v.getRol() == UserRolOnAd.BUYER).map(this::convertToDTO).sorted()
						.collect(Collectors.toList()));
	}

	@Override
	public void evaluate(final String tokenId, final ValuationForm valuationForm) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final String evaluator = userManagementProvider.retrieveUsernameByToken(tokenId);
		if (evaluator.equals(valuationForm.getEvaluated()))
			throw new UnauthorizedCommunicationException(ERROR_EVALUATED_CANT_BE_SAME_EVALUATOR);
		if (!userManagementProvider.existsUserByUsername(valuationForm.getEvaluated()))
			throw new NotFoundCommunicationException(ERROR_EVALUATED_USER_NOT_FOUND);
		final Valuation valuation = createValuation(valuationForm, evaluator);
		valuationRepository.save(valuation);
	}

	@Override
	public void sendMessage(final String tokenId, final MessageForm messageForm) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final String transmitter = userManagementProvider.retrieveUsernameByToken(tokenId);
		final Conversation conversation = conversationRepository.findById(messageForm.getConversationId())
				.orElseThrow(() -> new NotFoundCommunicationException(ERROR_CONVERSATION_NOT_FOUND));
		if (!(transmitter.equals(conversation.getUser1()) || transmitter.equals(conversation.getUser2())))
			throw new UnauthorizedCommunicationException(ERROR_YOU_ARENT_PARTICIPANT);

		final Message message = createMessage(transmitter, conversation, createText(messageForm.getText()));
		messageRepository.save(message);
	}

	@Override
	public Collection<ConversationDTO> listConversationsByParticipant(final String tokenId) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final String user = userManagementProvider.retrieveUsernameByToken(tokenId);
		return conversationRepository.findByUser1OrUser2(user, user).stream().map(this::convertToDTO).sorted()
				.collect(Collectors.toList());
	}

	@Override
	public MessagesListDTO getConversationWithMessagesByParticipants(final String tokenId, final String receiver) {
		userManagementProvider.comproveAuthorization(tokenId, RolUserType.REGISTERED_USER);
		final String transmitter = userManagementProvider.retrieveUsernameByToken(tokenId);
		if (transmitter.equals(receiver))
			throw new UnauthorizedCommunicationException(ERROR_RECEIVER_CANT_BE_SAME_TRANSMITTER);
		if (!userManagementProvider.existsUserByUsername(receiver))
			throw new NotFoundCommunicationException(ERROR_RECIEVER_NOT_FOUND);
		final Conversation conversation = getConversation(transmitter, receiver);
		final Collection<MessageDTO> messages = conversation.getMessage().stream().map(this::convertToDTO).sorted()
				.collect(Collectors.toList());
		return createMessagesListDTO(conversation.getId(), messages);
	}

	private Conversation getConversation(final String user1, final String user2) {
		Conversation conversation = conversationRepository.findByUser1AndUser2(user1, user2);
		if (conversation == null)
			conversation = conversationRepository.findByUser1AndUser2(user2, user1);
		if (conversation == null)
			conversation = createConversation(user1, user2);
		return conversation;
	}

	private ValuationDTO convertToDTO(final Valuation valuation) {
		final String text = valuation.getText() != null ? valuation.getText().getContent() : null;
		// @formatter:off
		return ValuationDTO.builder()
				.id(valuation.getId())
				.points(valuation.getPoints())
				.date(valuation.getDate())
				.evaluator(valuation.getEvaluator())
				.evaluated(valuation.getEvaluated())
				.text(text)
				.build();
		// @formatter:on
	}

	private MessageDTO convertToDTO(final Message message) {
		// @formatter:off
		return MessageDTO.builder()
				.author(message.getAuthor())
				.text(message.getText().getContent())
				.date(message.getDate())
				.build();
		// @formatter:on
	}

	private ConversationDTO convertToDTO(final Conversation conversation) {
		final LocalDateTime maxDate = conversation.getMessage().stream().map(Message::getDate)
				.max(LocalDateTime::compareTo).orElse(null);
		// @formatter:off
		return ConversationDTO.builder()
				.id(conversation.getId())
				.user1(conversation.getUser1())
				.user2(conversation.getUser2())
				.numberMessages(conversation.getMessage().size())
				.date(maxDate)
				.build();
		// @formatter:on
	}

	private Valuation createValuation(final ValuationForm valuationForm, final String evaluator) {
		final Text text = valuationForm.getText() != null ? createText(valuationForm.getText()) : null;
		// @formatter:off
		return Valuation.builder()
				.points(valuationForm.getPoints())
				.evaluator(evaluator)
				.evaluated(valuationForm.getEvaluated())
				.rol(valuationForm.getRol())
				.text(text)
				.date(LocalDateTime.now())
				.build();
		// @formatter:on
	}

	private Text createText(final String content) {
		final Text text = Text.builder().content(content).build();
		return textRepository.save(text);
	}

	private Conversation createConversation(final String user1, final String user2) {
		// @formatter:off
		final Conversation conversation = Conversation.builder()
											.user1(user1)
											.user2(user2)
											.build();
		// @formatter:on
		return conversationRepository.save(conversation);
	}

	private Message createMessage(final String author, final Conversation conversation, final Text text) {
		// @formatter:off
		return Message.builder()
				.author(author)
				.conversation(conversation)
				.text(text)
				.date(LocalDateTime.now())
				.build();
		// @formatter:on
	}

	private ValuationListsDTO createValuationListsDTO(final List<ValuationDTO> valuationsAsSeller,
			final List<ValuationDTO> valuationsAsBuyer) {
		// @formatter:off
		return ValuationListsDTO.builder()
				.valuationsAsSeller(valuationsAsSeller)
				.valuationsAsBuyer(valuationsAsBuyer)
				.build();
		// @formatter:on
	}

	private MessagesListDTO createMessagesListDTO(final Long conversationId, final Collection<MessageDTO> messages) {
		// @formatter:off
		return MessagesListDTO.builder()
				.conversationId(conversationId)
				.messages(messages)
				.build();
		// @formatter:on
	}
}
