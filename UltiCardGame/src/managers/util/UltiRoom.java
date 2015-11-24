package managers.util;

import interfaces.IMessageHandler;

import java.util.ArrayList;
import java.util.List;

import messagers.util.StartUltiAnswer;
import domain.ActivePlayer;

public class UltiRoom extends Room {

	public UltiRoom(final String name, final int maxSize) {
		super(name, maxSize);
	}

	@Override
	public boolean add(final ActivePlayer activePlayer) {
		final boolean inRoom = super.add(activePlayer);
		if (inRoom) {
			activePlayer.setUltiRoom(this);
		}
		return inRoom;
	}

	@Override
	public void remove(final ActivePlayer activePlayer) {
		super.remove(activePlayer);
		activePlayer.setUltiRoom(null);
	}

	public List<ActivePlayer> getAllPlayers() {
		return new ArrayList<ActivePlayer>(activePlayersInRoom);
	}

	public void sendStartMessageToAll(final IMessageHandler messageHandler) {
		final StartUltiAnswer answer = new StartUltiAnswer();
		super.sendToAll(messageHandler, answer);
	}

}
