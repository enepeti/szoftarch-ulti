package messagers;

import interfaces.managers.ISessionManager;
import interfaces.managers.IUltiRoomManager;
import interfaces.messagers.IMessageHandler;
import interfaces.messagers.IMessageReceiver;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import managers.SessionManager;
import managers.UltiRoomManager;
import domain.ActivePlayer;

@ServerEndpoint("/websocket/ulti")
public class MessageReceiver implements IMessageReceiver {

	private final ISessionManager sessionManager;
	private static IUltiRoomManager ultiRoomManager;
	private final IMessageHandler messageHandler = new MessageHandler();

	public MessageReceiver() {
		sessionManager = new SessionManager();
		ultiRoomManager = new UltiRoomManager();
	}

	@Override
	@OnOpen
	public void open(final Session session) {
		sessionManager.add(session);
		System.out.println("open");
	}

	@Override
	@OnClose
	public void close(final Session session) {
		final ActivePlayer activePlayer = sessionManager
				.getActivePlayer(session);
		if (activePlayer.getUltiRoom() != null) {
			if (activePlayer.getUltiRoom().isFull()) {
				ultiRoomManager.someoneLeavesRoom(activePlayer);
			} else {
				ultiRoomManager.deletePlayerFromRoom(activePlayer);
			}
		}

		sessionManager.remove(session);
	}

	@Override
	@OnError
	public void error(final Throwable error) {
		System.err.println(error);
	}

	@Override
	@OnMessage
	public void handleMessage(final String message, final Session session) {
		System.out.println("got: " + message);
		final ActivePlayer activePlayer = this.sessionManager
				.getActivePlayer(session);
		messageHandler.handle(message, activePlayer);
	}

}
