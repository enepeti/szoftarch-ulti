package managers;

import interfaces.managers.IRoomManager;

import java.util.Map;

import managers.util.Room;
import domain.ActivePlayer;

public abstract class RoomManager implements IRoomManager {
	protected static Map<String, ? extends Room> roomMap;

	@SuppressWarnings("unchecked")
	public Map<String, Room> getRoomMap() {
		return (Map<String, Room>) roomMap;
	}

	@Override
	public boolean addRoom(final Room room) {
		final String roomName = room.getName();
		if (getRoomMap().containsKey(roomName)) {
			return false;
		}

		getRoomMap().put(roomName, room);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Room> T getRoom(final String roomName) {
		return (T) roomMap.get(roomName);
	}

	@Override
	public void deleteRoom(final String roomName) {
		roomMap.remove(roomName);
	}

	@Override
	public void changePlayerRoom(final ActivePlayer activePlayer,
			final String toRoomName) {
		if (getRoomMap().containsKey(toRoomName)) {
			deletePlayerFromRoom(activePlayer);
		}

		addPlayerToRoom(activePlayer, toRoomName);
	}
}
