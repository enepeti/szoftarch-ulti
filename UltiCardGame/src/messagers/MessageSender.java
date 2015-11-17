package messagers;

import interfaces.IMessageSender;

import java.io.IOException;

import javax.websocket.Session;

import model.ActivePlayer;

public class MessageSender implements IMessageSender {
	
	@Override
	public void sendMessage(String message, ActivePlayer activePlayer) {
		Session session = activePlayer.getSession();
		
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
