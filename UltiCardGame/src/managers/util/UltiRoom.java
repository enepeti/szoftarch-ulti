package managers.util;

import interfaces.IMessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import messagers.util.PlayerOnTurnAnswer;
import messagers.util.ShowResultAnswer;
import messagers.util.StartUltiAnswer;
import messagers.util.StartUltiGameAnswer;
import ulti.UltiGame;
import domain.ActivePlayer;

public class UltiRoom extends Room {

	private UltiGame ultiGame;

	public UltiRoom(final String name, final int maxSize) {
		super(name, maxSize);
	}

	public UltiGame getUltiGame() {
		return ultiGame;
	}

	public void setUltiGame(final UltiGame ultiGame) {
		this.ultiGame = ultiGame;
	}

	@Override
	public boolean add(final ActivePlayer activePlayer) {
		final boolean inRoom = super.add(activePlayer);
		if (inRoom) {
			activePlayer.setUltiRoom(this);
		}
		return inRoom;
	}

	@Override
	public void remove(final ActivePlayer activePlayer) {
		super.remove(activePlayer);
		activePlayer.setUltiRoom(null);
	}

	public List<ActivePlayer> getAllPlayers() {
		return new ArrayList<ActivePlayer>(activePlayersInRoom);
	}

	public void sendStartMessageToAll(final IMessageHandler messageHandler) {
		final StartUltiAnswer answer = new StartUltiAnswer();
		super.sendToAll(messageHandler, answer);
	}

	public void sendNextPlayerOnTurnMessageToAll(
			final IMessageHandler messageHandler, final String name) {
		final PlayerOnTurnAnswer answer = new PlayerOnTurnAnswer(name);
		super.sendToAll(messageHandler, answer);
	}

	public void sendStartGameMessageToAll(final IMessageHandler messageHandler) {
		final StartUltiGameAnswer answer = new StartUltiGameAnswer();
		super.sendToAll(messageHandler, answer);
	}

	public void sendShowResultMessageToAll(
			final IMessageHandler messageHandler,
			final HashMap<String, Integer> points) {
		final ShowResultAnswer answer = new ShowResultAnswer(points);
		super.sendToAll(messageHandler, answer);
	}
}
