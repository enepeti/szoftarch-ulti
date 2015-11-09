package managers.util;

import interfaces.IMessageHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.Session;

import messagers.util.AnswerMessage;
import model.Player;

public abstract class Room {

	private Map<Session, Player> playersInRoom;
	private int maxSize;
	private String name;
	private boolean active;
	
	public Room(String name, int maxSize) {
		this.setName(name);
		this.maxSize = maxSize;
		this.playersInRoom = new HashMap<Session, Player>();
		this.active = true;
	}
	
	public boolean add(Session session, Player player) {
		if(playersInRoom.size() < maxSize || maxSize == -1) {
			playersInRoom.put(session, player);
			return true;
		}
		return false;
	}
	
	public void remove(Session session) {
		playersInRoom.remove(session);
		active = false;
	}
	
	protected void sendToAll(IMessageHandler messageHandler, AnswerMessage message) {
		for (Entry<Session, Player> session : this.getSessionsInRoom().entrySet()) {
			if(session.getValue() != null) {
				messageHandler.send(message, session.getKey());
			}
		}
	}

	public Map<Session, Player> getSessionsInRoom() {
		return playersInRoom;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}
}
