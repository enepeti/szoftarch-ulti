package managers;

import interfaces.ISessionManager;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import model.Player;

public class SessionManager implements ISessionManager {

	private static Map<Session, Player> sessions = new HashMap<Session, Player>();

	@Override
	public void add(final Session session) {
		sessions.put(session, null);
	}

	@Override
	public void remove(final Session session) {
		sessions.remove(session);
	}

	@Override
	public void setPlayer(Session session, Player player) {
		sessions.put(session, player);
		
	}

}