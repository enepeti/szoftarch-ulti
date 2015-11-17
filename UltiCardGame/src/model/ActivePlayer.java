package model;

import javax.websocket.Session;

import managers.util.ChatRoom;

public class ActivePlayer {

	private Player player;
	private Session session;
	private boolean loggedIn;
	private ChatRoom chatRoom;
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session currentSession) {
		this.session = currentSession;
	}
	public boolean isLoggedIn() {
		return loggedIn;
	}
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	public ChatRoom getChatRoom() {
		return chatRoom;
	}
	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}
	
}
