package interfaces;

import java.util.List;
import java.util.Set;

import javax.websocket.Session;

import domain.ActivePlayer;

public interface ISessionManager {

	public void add(Session session);

	public void remove(Session session);

	public ActivePlayer getActivePlayer(Session session);

	public Set<Session> getAllSession();

	public List<String> getAllActivePlayerNames();

	public void kickPlayer(String name, ActivePlayer admin);

	public ActivePlayer getActivePlayerForPlayerName(String name);

}
