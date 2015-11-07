package messageHandlers;

import javax.websocket.Session;

import interfaces.IMessageHandler;
import interfaces.IMessageReceiver;
import interfaces.ISessionRepository;

public class MessageReceiver implements IMessageReceiver {

	private ISessionRepository sessionRepository;
	private IMessageHandler messageHandler;
	
	public MessageReceiver(ISessionRepository sessionRepository, IMessageHandler messageHandler) {
		this.sessionRepository = sessionRepository;
		this.messageHandler = messageHandler;
	}
	
	@Override
	public void open(Session session) {
		sessionRepository.add(session);
	}

	@Override
	public void close(Session session) {
		sessionRepository.remove(session);

	}

	@Override
	public void error(Throwable error) {
		System.err.println(error.getMessage());

	}

	@Override
	public void handleMessage(String message, Session session) {
		messageHandler.handle(message, session);
	}

}
