package interfaces;

import javax.websocket.Session;

public interface IPlayerManager {

	public boolean login(String name, String pass, Session session);
	
	public boolean register(String name, String email, String pass);
	
}
