package interfaces;

import model.ActivePlayer;

public interface IRoomManager {
	public void addRoom(final String roomName, final int maxSize);

	public void deleteRoom(final String roomName);

	public void addPlayerToRoom(final ActivePlayer activePlayer,
			final String toRoomName);

	public void deletePlayerFromRoom(final ActivePlayer activePlayer);

	public void changePlayerRoom(final ActivePlayer activePlayer,
			final String toRoomName);
}
