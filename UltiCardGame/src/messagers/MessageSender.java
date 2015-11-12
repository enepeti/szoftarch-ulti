package messagers;

import java.io.IOException;

import interfaces.IMessageSender;
import interfaces.ISessionManager;

import javax.websocket.Session;

import managers.SessionManager;
import model.Player;

public class MessageSender implements IMessageSender {

	private final ISessionManager sessionManager = new SessionManager();
	
	@Override
	public void sendMessage(String message, Player player) {
		Session session = player.getCurrentSession();
		
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
