package managers;

import interfaces.IChatManager;
import interfaces.IMessageHandler;
import interfaces.ISessionManager;

import javax.websocket.Session;

import messagers.MessageHandler;
import messagers.util.ChatAnswer;
import model.Player;

public class ChatManager implements IChatManager {

	private ISessionManager sessionManager = new SessionManager();
	private IMessageHandler messageHandler = new MessageHandler();
	
	@Override
	public void Send(String message, Session session) {
		Player sender = sessionManager.getPlayer(session);
		for (Session toSession : sessionManager.getAllSession()) {
			if(sessionManager.getPlayer(toSession) != null) {
				messageHandler.send(new ChatAnswer(message, sender.getName()), toSession);
			}
		}
	}

}
