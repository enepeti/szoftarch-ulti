package managers;

import interfaces.IRoomManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import domain.ActivePlayer;
import managers.util.Room;

public abstract class RoomManager implements IRoomManager {
	protected static Map<String, ? extends Room> roomMap;

	@SuppressWarnings("unchecked")
	public Map<String, Room> getRoomMap() {
		return (Map<String, Room>) roomMap;
	}

	@Override
	public List<String> getAllRoomNames() {
		final ArrayList<String> roomNames = new ArrayList<String>();
		for (final Room room : roomMap.values()) {
			roomNames.add(room.getName());
		}

		return roomNames;
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
	public boolean changePlayerRoom(final ActivePlayer activePlayer,
			final String toRoomName) {
		if(getRoomMap().containsKey(toRoomName)) {
			deletePlayerFromRoom(activePlayer);
			addPlayerToRoom(activePlayer, toRoomName);
			return true;
		}
		
		return false;
	}
}
