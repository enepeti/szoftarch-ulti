package interfaces;

import javax.websocket.Session;

public interface IPlayerManager {

	public void login(String name, String pass, Session session);
	
	public void guestLogin(Session session);
	
	public void register(String name, String email, String pass, final Session session);
	
}
