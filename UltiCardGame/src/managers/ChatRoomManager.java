package managers;

import interfaces.IChatRoomManager;
import interfaces.IMessageHandler;
import managers.util.ChatRoom;
import messagers.MessageHandler;
import messagers.util.ErrorAnswer;
import model.ActivePlayer;

public class ChatRoomManager extends RoomManager implements IChatRoomManager {

	private final IMessageHandler messageHandler = new MessageHandler();
	// private static List<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
	private static ChatRoom globalChat;

	@Override
	public void Send(final String message, final ActivePlayer sender) {
		if (sender.isLoggedIn()) {
			sender.getChatRoom().sendMessageToAll(message,
					sender.getPlayer().getName(), messageHandler);
		} else {
			messageHandler.send(new ErrorAnswer(
					"Ismeretlen vagy számomra! Nem felejtettél belépni?"),
					sender);
		}
	}

	public static ChatRoom getGlobalChat() {
		if (globalChat == null) {
			globalChat = new ChatRoom("global", -1);
		}
		return globalChat;
	}

}
