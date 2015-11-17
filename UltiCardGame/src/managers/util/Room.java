package managers.util;

import interfaces.IMessageHandler;

import java.util.HashSet;
import java.util.Set;

import messagers.util.AnswerMessage;
import model.ActivePlayer;

public abstract class Room {

	private Set <ActivePlayer> activePlayersInRoom;
	private int maxSize;
	private String name;
	private boolean active;
	
	public Room(String name, int maxSize) {
		this.setName(name);
		this.maxSize = maxSize;
		this.activePlayersInRoom = new HashSet<ActivePlayer>();
		this.active = true;
	}
	
	public boolean add(ActivePlayer activePlayer) {
		if(activePlayersInRoom.size() < maxSize || maxSize == -1) {
			activePlayersInRoom.add(activePlayer);
			return true;
		}
		return false;
	}
	
	public void remove(ActivePlayer activePlayer) {
		activePlayersInRoom.remove(activePlayer);
		if(activePlayersInRoom.size() == 0) {
			active = false;
		}
	}
	
	protected void sendToAll(IMessageHandler messageHandler, AnswerMessage message) {
		for (ActivePlayer activePlayer : this.activePlayersInRoom) {
			if(activePlayer.isLoggedIn()) {
				messageHandler.send(message, activePlayer);
			}
		}
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
