package managers.util;

import domain.ActivePlayer;
import interfaces.IMessageHandler;
import messagers.util.ChatAnswer;

public class ChatRoom extends Room {

	public ChatRoom(final String name, final int maxSize) {
		super(name, maxSize);
	}

	@Override
	public boolean add(final ActivePlayer activePlayer) {
		final boolean inRoom = super.add(activePlayer);
		if (inRoom) {
			activePlayer.setChatRoom(this);
		}
		return inRoom;
	}

	public void sendMessageToAll(final String message, final String from,
			final IMessageHandler messageHandler) {
		final ChatAnswer answer = new ChatAnswer(message, from);
		super.sendToAll(messageHandler, answer);
	}

}
