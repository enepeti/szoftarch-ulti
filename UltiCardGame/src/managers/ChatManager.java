package managers;

import interfaces.IChatManager;
import interfaces.IMessageHandler;
import managers.util.ChatRoom;
import messagers.MessageHandler;
import messagers.util.ErrorAnswer;
import model.ActivePlayer;

public class ChatManager implements IChatManager {
	
	private IMessageHandler messageHandler = new MessageHandler();
	//private static List<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
	private static ChatRoom globalChat;
	
	@Override
	public void Send(String message, ActivePlayer sender) {
		if(sender.isLoggedIn()) {
			sender.getChatRoom().sendMessageToAll(message, sender.getPlayer().getName(), messageHandler);
		} else {
			messageHandler.send(new ErrorAnswer("Ismeretlen vagy számomra! Nem felejtettél belépni?"), sender);
		}
	}

	public static ChatRoom getGlobalChat() {
		if(globalChat == null)
			globalChat = new ChatRoom("global", -1);
		return globalChat;
	}

}
