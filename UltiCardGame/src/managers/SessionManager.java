package managers;

import interfaces.managers.IPlayerManager;
import interfaces.managers.ISessionManager;
import interfaces.messagers.IMessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.websocket.Session;

import messagers.MessageHandler;
import messagers.util.admin.KickAnswer;
import messagers.util.admin.KickPlayerAnswer;
import domain.ActivePlayer;
import domain.Player;

public class SessionManager implements ISessionManager {

	private final IPlayerManager playerManager = new PlayerManager();
	private final IMessageHandler messageHandler = new MessageHandler();
	private static Map<Session, ActivePlayer> sessions = new HashMap<Session, ActivePlayer>();

	@Override
	public void add(final Session session) {
		final ActivePlayer activePlayer = new ActivePlayer();
		activePlayer.setSession(session);
		activePlayer.setLoggedIn(false);
		sessions.put(session, activePlayer);
	}

	@Override
	public void remove(final Session session) {
		final ActivePlayer activePlayer = sessions.get(session);
		if (activePlayer != null) {
			activePlayer.setLoggedIn(false);
			if (activePlayer.getChatRoom() != null) {
				activePlayer.getChatRoom().remove(activePlayer);
			}
		}
		sessions.remove(session);
	}

	@Override
	public Set<Session> getAllSession() {
		return sessions.keySet();
	}

	@Override
	public List<String> getAllActivePlayerNames() {
		final List<String> nameList = new ArrayList<String>();
		for (final ActivePlayer activePlayer : sessions.values()) {
			final Player player = activePlayer.getPlayer();
			if (player != null) {
				nameList.add(player.getName());
			}
		}

		return nameList;
	}

	@Override
	public ActivePlayer getActivePlayer(final Session session) {
		return sessions.get(session);
	}

	@Override
	public void kickPlayer(final String name, final ActivePlayer admin) {
		final ActivePlayer activePlayerToKick = getActivePlayerForPlayerName(name);
		if ((activePlayerToKick != null)
				&& (activePlayerToKick.getPlayer() != null)) {
			playerManager.logout(activePlayerToKick);

			messageHandler.send(new KickPlayerAnswer(), activePlayerToKick);
			messageHandler.send(new KickAnswer(true), admin);
		} else {
			messageHandler.send(new KickAnswer(false), admin);
		}
	}

	@Override
	public ActivePlayer getActivePlayerForPlayerName(final String name) {
		for (final ActivePlayer activePlayer : sessions.values()) {
			final Player player = activePlayer.getPlayer();
			if (player != null) {
				if (player.getName().equals(name)) {
					return activePlayer;
				}
			}
		}

		return null;
	}

}
