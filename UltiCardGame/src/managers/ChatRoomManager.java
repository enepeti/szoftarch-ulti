package managers;

import interfaces.managers.IChatRoomManager;
import interfaces.messagers.IMessageHandler;

import java.util.HashMap;

import managers.util.ChatRoom;
import messagers.MessageHandler;
import messagers.util.chat.room.NewChatAnswer;
import messagers.util.chat.room.ToChatAnswer;
import messagers.util.error.ErrorAnswer;
import domain.ActivePlayer;

public class ChatRoomManager extends RoomManager implements IChatRoomManager {

	private final IMessageHandler messageHandler = new MessageHandler();
	public static final String globalChatName = "global";

	public ChatRoomManager() {
		roomMap = new HashMap<String, ChatRoom>();
	}

	@Override
	public void Send(final String message, final ActivePlayer sender) {
		if (sender.isLoggedIn()) {
			if (sender.getChatRoom() != null) {
				sender.getChatRoom().sendMessageToAll(message,
						sender.getPlayer().getName(), messageHandler);
			} else {
				messageHandler
						.send(new ErrorAnswer(
								"Nem vagy benn egy chat szobában sem! Így nem küldhetsz üzenetet!"),
								sender);
			}
		} else {
			messageHandler.send(new ErrorAnswer(
					"Ismeretlen vagy számomra! Nem felejtettél belépni?"),
					sender);
		}
	}

	@Override
	public boolean newRoom(final String roomName, final int maxSize,
			final ActivePlayer activePlayer) {
		final ChatRoom room = new ChatRoom(roomName, maxSize);

		if (super.addRoom(room)) {
			messageHandler.send(new NewChatAnswer(true), activePlayer);
			return true;
		}

		messageHandler.send(new NewChatAnswer(false), activePlayer);
		return false;
	}

	@Override
	public void deletePlayerFromRoom(final ActivePlayer activePlayer) {
		final ChatRoom actualRoom = activePlayer.getChatRoom();

		if (actualRoom != null) {
			actualRoom.remove(activePlayer);
		}
	}

	@Override
	public void addPlayerToRoom(final ActivePlayer activePlayer,
			final String toRoomName) {
		ChatRoom room = super.getRoom(toRoomName);

		if (toRoomName.equals(globalChatName) && (room == null)) {
			newRoom(globalChatName, -1, null);
			room = super.getRoom(globalChatName);
		}

		if (room != null) {
			if (room.add(activePlayer)) {
				messageHandler.send(new ToChatAnswer(toRoomName, true),
						activePlayer);
			} else {
				messageHandler.send(
						new ToChatAnswer("Tele van a szoba!", false),
						activePlayer);
			}
		} else {
			messageHandler.send(new ToChatAnswer(
					"Ilyen nevû szoba nem létezik!", false), activePlayer);
		}

	}

}
