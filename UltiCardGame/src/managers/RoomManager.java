package managers;

import interfaces.IRoomManager;

import java.util.Map;

import managers.util.Room;
import model.ActivePlayer;

public abstract class RoomManager implements IRoomManager {
	protected static Map<String, ? extends Room> roomMap;

	@SuppressWarnings("unchecked")
	public Map<String, Room> getRoomMap() {
		return (Map<String, Room>)roomMap;
	}
	
	@Override
	public boolean addRoom(Room room) {
		String roomName = room.getName();
		if(getRoomMap().containsKey(roomName)) {
			return false;
		}
		
		getRoomMap().put(roomName, room);
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Room> T getRoom(String roomName) {
		return (T) roomMap.get(roomName);
	}
	
	@Override
	public void deleteRoom(final String roomName) {
		roomMap.remove(roomName);
	}
	
	@Override
	public void changePlayerRoom(final ActivePlayer activePlayer,
			final String toRoomName) {
		deletePlayerFromRoom(activePlayer);
		addPlayerToRoom(activePlayer, toRoomName);
	}
}
