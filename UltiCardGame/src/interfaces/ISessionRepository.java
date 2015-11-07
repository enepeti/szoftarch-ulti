package interfaces;

import java.util.Set;

import javax.websocket.Session;

public interface ISessionRepository {
	
	public void add(Session session);
	
	public void remove(Session session);
	
	public Set<Session> list();
	
}
