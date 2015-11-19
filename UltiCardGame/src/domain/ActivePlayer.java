package domain;

import javax.websocket.Session;

import managers.util.ChatRoom;
import ulti.domain.UltiPlayer;

public class ActivePlayer {

	private Player player;
	private UltiPlayer ultiPlayer;
	private Session session;
	private boolean loggedIn;
	private ChatRoom chatRoom;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(final Player player) {
		this.player = player;
	}

	public UltiPlayer getUltiPlayer() {
		return ultiPlayer;
	}

	public void setUltiPlayer(final UltiPlayer ultiPlayer) {
		this.ultiPlayer = ultiPlayer;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(final Session currentSession) {
		this.session = currentSession;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(final boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public void setChatRoom(final ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

}
