package interfaces;

import javax.websocket.Session;

public interface IChatManager {

	public void Send(String message, Session session);
	
}
