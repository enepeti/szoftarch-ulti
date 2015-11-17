package managers.util;

import interfaces.IMessageHandler;
import messagers.util.ChatAnswer;
import model.ActivePlayer;


public class ChatRoom extends Room {

	public ChatRoom(String name, int maxSize) {
		super(name, maxSize);
	}
	
	@Override
	public boolean add(ActivePlayer activePlayer) {
		boolean inRoom = super.add(activePlayer);
		if(inRoom) {
			activePlayer.setChatRoom(this);
		}
		return inRoom;
	}
	
	public void sendMessageToAll(String message, String from, IMessageHandler messageHandler) {
		ChatAnswer answer = new ChatAnswer(message, from);
		super.sendToAll(messageHandler, answer);
	}
	
	
}
