package managers;

import interfaces.IMessageHandler;
import interfaces.IUltiRoomManager;
import managers.util.UltiRoom;
import messagers.MessageHandler;
import messagers.util.NewUltiAnswer;
import messagers.util.ToUltiAnswer;
import ulti.UltiGame;
import domain.ActivePlayer;

public class UltiRoomManager extends RoomManager implements IUltiRoomManager {

	private final IMessageHandler messageHandler = new MessageHandler();
	private UltiGame ultiGame;

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
					ultiGame = new UltiGame(room.getAllPlayers());
					room.setUltiGame(ultiGame);
					room.sendStartMessageToAll(messageHandler);
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
