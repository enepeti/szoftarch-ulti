package managers;

import interfaces.IRoomManager;

import java.util.HashMap;
import java.util.Map;

import managers.util.ChatRoom;
import managers.util.Room;
import model.ActivePlayer;

public abstract class RoomManager implements IRoomManager {
	private final Map<String, Room> roomMap;

	public RoomManager() {
		roomMap = new HashMap<String, Room>();
	}

	public Map<String, Room> getRoomMap() {
		return roomMap;
	}

	@Override
	public void addRoom(final String roomName, final int maxSize) {
		final Room room = new Room(roomName, maxSize);

		roomMap.put(roomName, room);
	}

	@Override
	public void deleteRoom(final String roomName) {
		roomMap.remove(roomName);
	}

	@Override
	public void addPlayerToRoom(final ActivePlayer activePlayer,
			final String toRoomName) {
		final Room room = roomMap.get(toRoomName);
		if (room != null) {
			room.add(activePlayer);
			// TODO: ne kaszttal
			activePlayer.setChatRoom((ChatRoom) room);
		}
	}

	@Override
	public void deletePlayerFromRoom(final ActivePlayer activePlayer) {
		activePlayer.getChatRoom().remove(activePlayer);
		activePlayer.setChatRoom(null);
	}

	@Override
	public void changePlayerRoom(final ActivePlayer activePlayer,
			final String toRoomName) {
		deletePlayerFromRoom(activePlayer);
		addPlayerToRoom(activePlayer, toRoomName);
	}
}
