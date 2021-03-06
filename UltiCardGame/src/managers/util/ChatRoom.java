package managers.util;

import domain.ActivePlayer;
import interfaces.messagers.IMessageHandler;
import messagers.util.chat.ChatAnswer;

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
	
	@Override
	public void remove(ActivePlayer activePlayer) {
		super.remove(activePlayer);
		activePlayer.setChatRoom(null);
	}

	public void sendMessageToAll(final String message, final String from,
			final IMessageHandler messageHandler) {
		final ChatAnswer answer = new ChatAnswer(message, from);
		super.sendToAll(messageHandler, answer);
	}

}
