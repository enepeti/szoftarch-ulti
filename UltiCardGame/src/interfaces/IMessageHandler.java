package interfaces;

import javax.websocket.Session;

public interface IMessageHandler {

	public void handle(String message, Session session);
	
	public <T> void send(T messageObject, Session session);
	
}
