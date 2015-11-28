package managers.util;

import interfaces.messagers.IMessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import messagers.util.ulti.GameSelectedAnswer;
import messagers.util.ulti.PlayedCardAnswer;
import messagers.util.ulti.PlayerOnTurnAnswer;
import messagers.util.ulti.ShowPartyResultAnswer;
import messagers.util.ulti.ShowResultAnswer;
import messagers.util.ulti.StartUltiAnswer;
import messagers.util.ulti.StartUltiGameAnswer;
import messagers.util.ulti.TakeCardsAnswer;
import ulti.UltiGame;
import ulti.domain.Card;
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
		return new ArrayList<ActivePlayer>(getActivePlayersInRoom());
	}

	public void sendStartMessageToAll(final IMessageHandler messageHandler) {
		final StartUltiAnswer answer = new StartUltiAnswer();
		super.sendToAll(messageHandler, answer);
	}

	public void sendNextPlayerOnTurnMessageToAllOthers(
			final IMessageHandler messageHandler, final String name,
			final ActivePlayer activePlayer) {
		final PlayerOnTurnAnswer answer = new PlayerOnTurnAnswer(name, false);
		super.sendToAllOthers(messageHandler, answer, activePlayer);
	}

	public void sendStartGameMessageToAll(final IMessageHandler messageHandler,
			final HashMap<String, Integer> points) {
		final StartUltiGameAnswer answer = new StartUltiGameAnswer(points);
		super.sendToAll(messageHandler, answer);
	}

	public void sendShowResultMessageToAll(
			final IMessageHandler messageHandler,
			final HashMap<String, Integer> points) {
		final ShowResultAnswer answer = new ShowResultAnswer(points);
		super.sendToAll(messageHandler, answer);
	}

	public void sendPlayedCardMessageToAllOthers(
			final IMessageHandler messageHandler, final String name,
			final Card card, final ActivePlayer activePlayer) {
		final PlayedCardAnswer answer = new PlayedCardAnswer(name, false, card);
		super.sendToAllOthers(messageHandler, answer, activePlayer);
	}

	public void sendTakenCardsMessageToAllOthers(
			final IMessageHandler messageHandler, final String name,
			final List<Card> cardsOnTable, final ActivePlayer activePlayer) {
		final TakeCardsAnswer answer = new TakeCardsAnswer(name, false,
				cardsOnTable);
		super.sendToAllOthers(messageHandler, answer, activePlayer);
	}

	public void sendGameSelectionMessageToAllOthers(
			final IMessageHandler messageHandler, final String name,
			final int convertConcreteGameTypeToInt,
			final ActivePlayer activePlayer) {
		final GameSelectedAnswer answer = new GameSelectedAnswer(name, false,
				convertConcreteGameTypeToInt);
		super.sendToAllOthers(messageHandler, answer, activePlayer);
	}

	public void sendShowPartyResultMessageToAll(
			final IMessageHandler messageHandler,
			final HashMap<String, Integer> partyPoints) {
		final ShowPartyResultAnswer answer = new ShowPartyResultAnswer(
				partyPoints);
		super.sendToAll(messageHandler, answer);
	}
}
