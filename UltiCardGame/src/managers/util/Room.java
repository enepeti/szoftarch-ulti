package managers.util;

import interfaces.IMessageHandler;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.websocket.Session;

import messagers.util.AnswerMessage;
import model.Player;

public abstract class Room {

	private Set <Player> playersInRoom;
	private int maxSize;
	private String name;
	private boolean active;
	
	public Room(String name, int maxSize) {
		this.setName(name);
		this.maxSize = maxSize;
		this.playersInRoom = new HashSet<Player>();
		this.active = true;
	}
	
	public boolean add(Player player) {
		if(playersInRoom.size() < maxSize || maxSize == -1) {
			playersInRoom.add(player);
			return true;
		}
		return false;
	}
	
	public void remove(Player player) {
		playersInRoom.remove(player);
		active = false;
	}
	
	protected void sendToAll(IMessageHandler messageHandler, AnswerMessage message) {
		for (Player player : this.playersInRoom) {
			if(player.isLoggedIn()) {
				messageHandler.send(message, player);
			}
		}
	}

//	public Map<Session, Player> getSessionsInRoom() {
//		return playersInRoom;
//	}
	
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
