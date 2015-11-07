package messageHandlers;

import javax.websocket.Session;

import interfaces.IMessageHandler;
import interfaces.IPlayerManager;

public class MessageHandler implements IMessageHandler {

	private static IPlayerManager playerManager;
	
	public MessageHandler(IPlayerManager playerManager) {
		this.playerManager = playerManager;
	}
	
	@Override
	public void handle(String message, Session session) {
		// dejason(message);
		// message.type == login
		// playerManager.login(name, pw);
		
	}

	@Override
	public <T> void send(T messageObject, Session session) {
		// TODO Auto-generated method stub
		
	}

}
