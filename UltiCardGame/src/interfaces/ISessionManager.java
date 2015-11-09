package interfaces;

import javax.websocket.Session;

import model.Player;

public interface ISessionManager {
	
	public void add(Session session);
	
	public void remove(Session session);
	
	public void setPlayer(Session session, Player player);
	
}
