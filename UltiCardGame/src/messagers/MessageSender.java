package messagers;

import interfaces.messagers.IMessageSender;

import java.io.IOException;

import javax.websocket.Session;

import loging.Logger;
import loging.StdLogger;
import domain.ActivePlayer;

public class MessageSender implements IMessageSender {

	private static Logger logger = new StdLogger();
	
	@Override
	public void sendMessage(final String message,
			final ActivePlayer activePlayer) {
		final Session session = activePlayer.getSession();

		try {
			logger.log("sent: " + message);
			session.getBasicRemote().sendText(message);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
