package managers;

import interfaces.ISessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.websocket.Session;

import model.ActivePlayer;

public class SessionManager implements ISessionManager {

	private static Map<Session, ActivePlayer> sessions = new HashMap<Session, ActivePlayer>();

	@Override
	public void add(final Session session) {
		ActivePlayer activePlayer = new ActivePlayer();
		activePlayer.setSession(session);
		activePlayer.setLoggedIn(false);
		sessions.put(session, activePlayer);
	}

	@Override
	public void remove(final Session session) {
		ActivePlayer activePlayer = sessions.get(session);
		if(activePlayer != null) {
			activePlayer.setLoggedIn(false);
			if(activePlayer.getChatRoom() != null) {
				activePlayer.getChatRoom().remove(activePlayer);
			}
		}
		sessions.remove(session);
	}

//	@Override
//	public void setPlayer(Session session, Player player) {
//		sessions.put(session, player);
//		
//	}

	@Override
	public Set<Session> getAllSession() {
		return sessions.keySet();
	}

	@Override
	public ActivePlayer getActivePlayer(Session session) {
		return sessions.get(session);
	}

}
