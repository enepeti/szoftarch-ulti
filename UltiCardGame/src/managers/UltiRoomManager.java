package managers;

import interfaces.managers.IUltiRoomManager;
import interfaces.messagers.IMessageHandler;

import java.util.ArrayList;
import java.util.List;

import managers.util.Room;
import managers.util.UltiRoom;
import messagers.MessageHandler;
import messagers.util.ulti.room.NewUltiAnswer;
import messagers.util.ulti.room.ToUltiAnswer;
import ulti.UltiGame;
import domain.ActivePlayer;

public class UltiRoomManager extends RoomManager implements IUltiRoomManager {

	private final IMessageHandler messageHandler = new MessageHandler();
	private UltiGame ultiGame;

	@Override
	public List<String> getAllRoomNames() {
		final ArrayList<String> roomNames = new ArrayList<String>();
		for (final Room room : roomMap.values()) {
			if (room instanceof UltiRoom) {
				roomNames.add(room.getName());
			}
		}

		return roomNames;
	}

	@Override
	public boolean newRoom(final String roomName, final int maxSize,
			final ActivePlayer activePlayer) {
		final UltiRoom room = new UltiRoom(roomName, maxSize);

		if (super.addRoom(room)) {
			messageHandler.send(new NewUltiAnswer(true), activePlayer);
			return true;
		}

		messageHandler.send(new NewUltiAnswer(false), activePlayer);
		return false;
	}

	@Override
	public void addPlayerToRoom(final ActivePlayer activePlayer,
			final String toRoomName) {
		final UltiRoom room = super.getRoom(toRoomName);

		if (room != null) {
			if (room.add(activePlayer)) {
				messageHandler.send(new ToUltiAnswer(toRoomName, true),
						activePlayer);
				if (room.isFull()) {
					room.sendStartMessageToAll(messageHandler);
					ultiGame = new UltiGame(room.getAllPlayers());
					room.setUltiGame(ultiGame);
				}
			} else {
				messageHandler.send(
						new ToUltiAnswer("Tele van a szoba!", false),
						activePlayer);
			}
		} else {
			messageHandler.send(new ToUltiAnswer(
					"Ilyen nevû szoba nem létezik!", false), activePlayer);
		}

	}

	@Override
	public void deletePlayerFromRoom(final ActivePlayer activePlayer) {
		final UltiRoom room = activePlayer.getUltiRoom();

		if (room != null) {
			room.remove(activePlayer);
		}
	}

}
