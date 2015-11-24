package managers.util;

import interfaces.IMessageHandler;

import java.util.HashSet;
import java.util.Set;

import domain.ActivePlayer;
import messagers.util.AnswerMessage;

public abstract class Room {

	protected final Set<ActivePlayer> activePlayersInRoom;
	private final int maxSize;
	private String name;
	private boolean active;

	public Room(final String name, final int maxSize) {
		this.setName(name);
		this.maxSize = maxSize;
		this.activePlayersInRoom = new HashSet<ActivePlayer>();
		this.active = true;
	}

	public boolean add(final ActivePlayer activePlayer) {
		if (!isFull()) {
			activePlayersInRoom.add(activePlayer);
			return true;
		}
		return false;
	}

	public void remove(final ActivePlayer activePlayer) {
		activePlayersInRoom.remove(activePlayer);
		if (activePlayersInRoom.size() == 0) {
			active = false;
		}
	}

	protected void sendToAll(final IMessageHandler messageHandler,
			final AnswerMessage message) {
		for (final ActivePlayer activePlayer : this.activePlayersInRoom) {
			if (activePlayer.isLoggedIn()) {
				messageHandler.send(message, activePlayer);
			}
		}
	}

	public boolean isFull() {
		return (activePlayersInRoom.size() >= maxSize) && (maxSize != -1);
	}
	
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isActive() {
		return active;
	}
}
