package managers;

import interfaces.IChatRoomManager;
import interfaces.IMessageHandler;

import java.util.HashMap;

import domain.ActivePlayer;
import managers.util.ChatRoom;
import messagers.MessageHandler;
import messagers.util.ErrorAnswer;

public class ChatRoomManager extends RoomManager implements IChatRoomManager {

	private final IMessageHandler messageHandler = new MessageHandler();
	public static final String globalChatName = "global";

	public ChatRoomManager() {
		roomMap = new HashMap<String, ChatRoom>();
	}
	
	@Override
	public void Send(final String message, final ActivePlayer sender) {
		if (sender.isLoggedIn()) {
			if(sender.getChatRoom() != null) {
				sender.getChatRoom().sendMessageToAll(message,
						sender.getPlayer().getName(), messageHandler);
			} else {
				messageHandler.send(new ErrorAnswer(
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
	public boolean newRoom(String roomName, int maxSize) {
		ChatRoom room = new ChatRoom(roomName, maxSize);

		return super.addRoom(room);
	}

	@Override
	public void deletePlayerFromRoom(ActivePlayer activePlayer) {
		ChatRoom actualRoom = activePlayer.getChatRoom();
		
		if(actualRoom != null) {
			activePlayer.setChatRoom(null);
			actualRoom.remove(activePlayer);
		}
	}

	@Override
	public void addPlayerToRoom(ActivePlayer activePlayer, String toRoomName) {
		ChatRoom room = super.getRoom(toRoomName);
		
		if(toRoomName.equals(globalChatName) && room == null) {
			newRoom(globalChatName, -1);
			room = super.getRoom(globalChatName);
		}
		
		if(room != null) {
			room.add(activePlayer);
			//siker
		}
		//fail
		
	}
	
}
