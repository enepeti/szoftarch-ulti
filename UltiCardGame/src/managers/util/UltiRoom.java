package managers.util;

import java.util.ArrayList;
import java.util.List;

import domain.ActivePlayer;

public class UltiRoom extends Room {

	public UltiRoom(String name, int maxSize) {
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
	public void remove(ActivePlayer activePlayer) {
		super.remove(activePlayer);
		activePlayer.setUltiRoom(null);
	}
	
	public List<ActivePlayer> getAllPlayers() {
		return new ArrayList<ActivePlayer>(activePlayersInRoom);
	}
	
}
