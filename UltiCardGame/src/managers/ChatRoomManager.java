package managers;

import interfaces.managers.IChatRoomManager;
import interfaces.messagers.IMessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import managers.util.ChatRoom;
import managers.util.Room;
import messagers.MessageHandler;
import messagers.util.chat.room.ChatRoomSizeShower;
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
	public List<ChatRoomSizeShower> getAllChatRooms() {
		final ArrayList<ChatRoomSizeShower> chatRoomSizeShowers = new ArrayList<ChatRoomSizeShower>();
		for (final Room room : roomMap.values()) {
			if (room instanceof ChatRoom) {
				final ChatRoomSizeShower chatRoomSizeShower = new ChatRoomSizeShower();
				chatRoomSizeShower.setName(room.getName());
				chatRoomSizeShower.setActual(room.getActualSize());
				chatRoomSizeShower.setMax(room.getMaxSize());

				chatRoomSizeShowers.add(chatRoomSizeShower);
			}
		}

		return chatRoomSizeShowers;
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
								"Nem vagy benn egy chat szob�ban sem! �gy nem k�ldhetsz �zenetet!"),
								sender);
			}
		} else {
			messageHandler.send(new ErrorAnswer(
					"Ismeretlen vagy sz�momra! Nem felejtett�l bel�pni?"),
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
					"Ilyen nev� szoba nem l�tezik!", false), activePlayer);
		}

	}

}
