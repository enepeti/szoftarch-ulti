package messagers;

import interfaces.messagers.IMessageSender;

import java.io.IOException;

import javax.websocket.Session;

import domain.ActivePlayer;

public class MessageSender implements IMessageSender {

	@Override
	public void sendMessage(final String message,
			final ActivePlayer activePlayer) {
		final Session session = activePlayer.getSession();

		try {
			session.getBasicRemote().sendText(message);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
