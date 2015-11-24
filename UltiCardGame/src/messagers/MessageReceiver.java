package messagers;

import interfaces.managers.ISessionManager;
import interfaces.messagers.IMessageHandler;
import interfaces.messagers.IMessageReceiver;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import domain.ActivePlayer;
import managers.SessionManager;

@ServerEndpoint("/websocket/ulti")
public class MessageReceiver implements IMessageReceiver {

	private final ISessionManager sessionRepository;
	private final IMessageHandler messageHandler = new MessageHandler();

	public MessageReceiver() {
		sessionRepository = new SessionManager();
	}

	@Override
	@OnOpen
	public void open(final Session session) {
		sessionRepository.add(session);
		System.out.println("open");
	}

	@Override
	@OnClose
	public void close(final Session session) {
		sessionRepository.remove(session);
	}

	@Override
	@OnError
	public void error(final Throwable error) {
		System.err.println(error.getMessage());
	}

	@Override
	@OnMessage
	public void handleMessage(final String message, final Session session) {
		System.out.println(message);
		ActivePlayer activePlayer = this.sessionRepository.getActivePlayer(session);
		messageHandler.handle(message, activePlayer);
	}

}
