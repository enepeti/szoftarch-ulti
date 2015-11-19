package interfaces;

import managers.util.Room;
import model.ActivePlayer;

public interface IRoomManager {
	
	public boolean addRoom(Room room);
	
	public boolean newRoom(final String roomName, final int maxSize);

	public void deleteRoom(final String roomName);

	public void addPlayerToRoom(final ActivePlayer activePlayer,
			final String toRoomName);

	public void deletePlayerFromRoom(final ActivePlayer activePlayer);

	public void changePlayerRoom(final ActivePlayer activePlayer,
			final String toRoomName);

	public <T extends Room> T getRoom(String roomName);

}
