package managers.util;

import interfaces.IMessageHandler;

import javax.websocket.Session;

import messagers.util.ChatAnswer;
import model.Player;


public class ChatRoom extends Room {

	public ChatRoom(String name, int maxSize) {
		super(name, maxSize);
	}
	
	@Override
	public boolean add(Session session, Player player) {
		boolean inRoom = super.add(session, player);
		if(inRoom) {
			player.setChatRoom(this);
		}
		return inRoom;
	}
	
	public void sendMessageToAll(String message, String from, IMessageHandler messageHandler) {
		ChatAnswer answer = new ChatAnswer(message, from);
		super.sendToAll(messageHandler, answer);
	}
	
	
}
