package interfaces;

import java.util.Set;

import javax.websocket.Session;

import model.ActivePlayer;

public interface ISessionManager {
	
	public void add(Session session);
	
	public void remove(Session session);
	
	public ActivePlayer getActivePlayer(Session session);
	
	public Set<Session> getAllSession();
	
}
