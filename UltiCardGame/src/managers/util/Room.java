package managers.util;

import interfaces.messagers.IMessageHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import messagers.util.AnswerMessage;
import domain.ActivePlayer;

public abstract class Room {

	private final Set<ActivePlayer> activePlayersInRoom;
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
			getActivePlayersInRoom().add(activePlayer);
			return true;
		}

		return false;
	}

	public void remove(final ActivePlayer activePlayer) {
		getActivePlayersInRoom().remove(activePlayer);
		if (getActivePlayersInRoom().isEmpty()) {
			active = false;
		}
	}

	protected void sendToAll(final IMessageHandler messageHandler,
			final AnswerMessage message) {
		for (final ActivePlayer activePlayer : this.getActivePlayersInRoom()) {
			if (activePlayer.isLoggedIn()) {
				messageHandler.send(message, activePlayer);
			}
		}
	}

	public boolean isFull() {
		return (getActivePlayersInRoom().size() >= maxSize) && (maxSize != -1);
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

	public Set<ActivePlayer> getActivePlayersInRoom() {
		return activePlayersInRoom;
	}

	public List<String> getActivePlayerNamesInRoom() {
		final List<String> names = new ArrayList<String>();
		for (final ActivePlayer activePlayer : activePlayersInRoom) {
			names.add(activePlayer.getPlayer().getName());
		}

		return names;
	}
}
