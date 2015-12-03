package interfaces.managers;

import managers.util.Room;
import domain.ActivePlayer;

public interface IRoomManager {

	public boolean addRoom(Room room);

	public boolean newRoom(String roomName, int maxSize,
			ActivePlayer activePlayer);

	public void deleteRoom(final String roomName);

	public void addPlayerToRoom(final ActivePlayer activePlayer,
			final String toRoomName);

	public void deletePlayerFromRoom(final ActivePlayer activePlayer);

	public void changePlayerRoom(final ActivePlayer activePlayer,
			final String toRoomName);

	public <T extends Room> T getRoom(String roomName);

}
