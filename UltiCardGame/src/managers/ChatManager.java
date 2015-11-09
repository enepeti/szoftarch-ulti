package managers;

import interfaces.IChatManager;
import interfaces.IMessageHandler;
import interfaces.ISessionManager;

import javax.websocket.Session;

import managers.util.ChatRoom;
import messagers.MessageHandler;
import messagers.util.ErrorAnswer;
import model.Player;

public class ChatManager implements IChatManager {

	private ISessionManager sessionManager = new SessionManager();
	private IMessageHandler messageHandler = new MessageHandler();
	//private static List<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
	private static ChatRoom globalChat;
	
	@Override
	public void Send(String message, Session session) {
		Player sender = sessionManager.getPlayer(session);
		if(sender != null) {
			sender.getChatRoom().sendMessageToAll(message, sender.getName(), messageHandler);
		} else {
			messageHandler.send(new ErrorAnswer("Ismeretlen vagy számomra! Nem felejtettél belépni?"), session);
		}
	}

	public static ChatRoom getGlobalChat() {
		if(globalChat == null)
			globalChat = new ChatRoom("global", -1);
		return globalChat;
	}

}
