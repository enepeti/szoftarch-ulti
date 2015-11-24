package ulti;

import interfaces.IMessageHandler;
import interfaces.IPlayerRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import messagers.MessageHandler;
import messagers.util.DealAnswer;
import messagers.util.HasToConfirmAnswer;
import messagers.util.PickUpCardsAnswer;
import messagers.util.TakeCardsAnswer;
import ulti.domain.Card;
import ulti.domain.DeckOfCards;
import ulti.domain.gametype.ConcreteGameType;
import dal.PlayerRepository;
import domain.ActivePlayer;
import domain.Player;
import domain.PlayerTypeClass.PlayerType;

public class UltiGame {

	private final List<ActivePlayer> activePlayerList;
	private int starterActivePlayer = 0;
	private int activePlayerOnTurn = 0;
	private int lastPlayerWithConcreteGameType = 0;
	private Card remainingCard1 = null;
	private Card remainingCard2 = null;
	private final List<Card> cardsOnTable = null;
	private ConcreteGameType concreteGameType = null;
	private DeckOfCards deckOfCards;

	private final IPlayerRepository playerRepository = new PlayerRepository();
	private final IMessageHandler messageHandler = new MessageHandler();

	public UltiGame(final List<ActivePlayer> activePlayerList) {
		this.activePlayerList = activePlayerList;
		this.deckOfCards = new DeckOfCards();

		randomizeStarterPlayer();
		deal();
	}

	public int getStarterActivePlayer() {
		return starterActivePlayer;
	}

	public void setStarterActivePlayer(final int starterActivePlayer) {
		this.starterActivePlayer = starterActivePlayer;
	}

	public int getActivePlayerOnTurn() {
		return activePlayerOnTurn;
	}

	public void setActivePlayerOnTurn(final int activePlayerOnTurn) {
		this.activePlayerOnTurn = activePlayerOnTurn;
	}

	public int getLastPlayerWithConcreteGameType() {
		return lastPlayerWithConcreteGameType;
	}

	public void setLastPlayerWithConcreteGameType(
			final int lastPlayerWithConcreteGameType) {
		this.lastPlayerWithConcreteGameType = lastPlayerWithConcreteGameType;
	}

	public Card getRemainingCard1() {
		return remainingCard1;
	}

	public void setRemainingCard1(final Card remainingCard1) {
		this.remainingCard1 = remainingCard1;
	}

	public Card getRemainingCard2() {
		return remainingCard2;
	}

	public void setRemainingCard2(final Card remainingCard2) {
		this.remainingCard2 = remainingCard2;
	}

	public ConcreteGameType getConcreteGameType() {
		return concreteGameType;
	}

	public void setConcreteGameType(final ConcreteGameType concreteGameType) {
		this.concreteGameType = concreteGameType;
	}

	public List<ActivePlayer> getActivePlayerList() {
		return activePlayerList;
	}

	public DeckOfCards getDeckOfCards() {
		return deckOfCards;
	}

	public void setDeckOfCards(final DeckOfCards deckOfCards) {
		this.deckOfCards = deckOfCards;
	}

	private void randomizeStarterPlayer() {
		final Random rand = new Random();
		final int min = 0;
		final int max = 2;

		final int randomNum = rand.nextInt((max - min) + 1) + min;

		starterActivePlayer = randomNum;
		activePlayerOnTurn = starterActivePlayer;
	}

	private void incrementStarterPlayer() {
		if (starterActivePlayer == 2) {
			starterActivePlayer = 0;
		} else {
			starterActivePlayer++;
		}
	}

	private void incrementActivePlayerOnTurn() {
		if (activePlayerOnTurn == 2) {
			activePlayerOnTurn = 0;
		} else {
			activePlayerOnTurn++;
		}
	}

	public void deal() {
		deckOfCards.shuffle();

		final List<Card> cards = deckOfCards.getCards();

		final ActivePlayer activePlayer = activePlayerList
				.get(starterActivePlayer);
		final List<Card> subList = cards.subList(0, 11);
		activePlayer.getUltiPlayer().addCardsToHand(subList);
		messageHandler.send(new DealAnswer(subList, true), activePlayer);

		incrementStarterPlayer();
		final ActivePlayer activePlayer2 = activePlayerList
				.get(starterActivePlayer);
		final List<Card> subList2 = cards.subList(12, 21);
		activePlayer.getUltiPlayer().addCardsToHand(subList2);
		messageHandler.send(new DealAnswer(subList2, false), activePlayer2);

		incrementStarterPlayer();
		final ActivePlayer activePlayer3 = activePlayerList
				.get(starterActivePlayer);
		final List<Card> subList3 = cards.subList(22, 31);
		activePlayer.getUltiPlayer().addCardsToHand(subList3);
		messageHandler.send(new DealAnswer(subList3, false), activePlayer3);

		incrementStarterPlayer();
	}

	public void say(final ConcreteGameType concreteGameType, final Card card1,
			final Card card2) {
		remainingCard1 = card1;
		remainingCard2 = card2;
		this.concreteGameType = concreteGameType;
		lastPlayerWithConcreteGameType = activePlayerOnTurn;
		nextPlayerTurn();
	}

	public void pass() {
		nextPlayerTurn();
	}

	public void pickUpCards() {
		final List<Card> cardsToHand = new ArrayList<Card>();
		cardsToHand.add(remainingCard1);
		cardsToHand.add(remainingCard2);
		final ActivePlayer activePlayer = activePlayerList
				.get(activePlayerOnTurn);
		activePlayer.getUltiPlayer().addCardsToHand(cardsToHand);
		messageHandler.send(new PickUpCardsAnswer(remainingCard1,
				remainingCard2), activePlayer);
		remainingCard1 = null;
		remainingCard2 = null;
	}

	public void confirm() {
		startGame();
	}

	public void nextPlayerTurn() {
		incrementActivePlayerOnTurn();

		final ActivePlayer activePlayer = activePlayerList
				.get(activePlayerOnTurn);
		activePlayer.getUltiRoom().sendNextPlayerOnTurnMessageToAll(
				messageHandler, activePlayer.getPlayer().getName());

		if (lastPlayerWithConcreteGameType == activePlayerOnTurn) {
			messageHandler.send(new HasToConfirmAnswer(), activePlayer);
		}
	}

	public void startGame() {
		activePlayerList.get(0).getUltiRoom()
				.sendStartGameMessageToAll(messageHandler);
	}

	public void playCard(final Card card) {
		cardsOnTable.add(card);
		if (cardsOnTable.size() == 3) {
			evaluateTurn();
		}

		nextPlayerTurn();
	}

	private void nextPlayerTurnAfterEvaluation() {

	}

	public void endGame() {

	}

	public void nextGame() {
		incrementStarterPlayer();
		deal();
		activePlayerOnTurn = starterActivePlayer;
	}

	public void evaluateTurn() {
		// TODO: szabályok alapján a megfelelõ játékos vigye: x
		final int x = 0;
		final ActivePlayer activePlayer = activePlayerList.get(x);
		activePlayer.getUltiPlayer().addCardsToTaken(cardsOnTable);
		messageHandler.send(new TakeCardsAnswer(cardsOnTable), activePlayer);

		if (activePlayer.getUltiPlayer().getHand().isEmpty()) {
			evaluateGame();
		} else {
			activePlayerOnTurn = x;
			nextPlayerTurnAfterEvaluation();
		}
	}

	private void evaluateGame() {
		// TODO: játék pontok számolása
		int sumForPlayer0 = 0;
		int sumForPlayer1 = 0;
		int sumForPlayer2 = 0;

		if (concreteGameType.isThereParty()) {
			int sumForPlayer = 0;
			int sumForOpponents = 0;
			for (int i = 0; i < 3; i++) {
				final int sum = activePlayerList.get(i).getUltiPlayer()
						.calculateParty();
				if (i == lastPlayerWithConcreteGameType) {
					sumForPlayer += sum;
				} else {
					sumForOpponents += sum;
				}
			}

			sumForPlayer0 += sumForPlayer;
			sumForPlayer1 += sumForOpponents;
			sumForPlayer2 += sumForOpponents;
		}

		final Player player0 = activePlayerList.get(0).getPlayer();
		player0.addPoint(sumForPlayer0);
		if (player0.getType() != PlayerType.GUEST) {
			savePoints(player0);
		}

		final Player player1 = activePlayerList.get(1).getPlayer();
		player1.addPoint(sumForPlayer1);
		if (player1.getType() != PlayerType.GUEST) {
			savePoints(player1);
		}

		final Player player2 = activePlayerList.get(2).getPlayer();
		player2.addPoint(sumForPlayer2);
		if (player2.getType() != PlayerType.GUEST) {
			savePoints(player2);
		}

		final HashMap<String, Integer> points = new HashMap<String, Integer>();
		points.put(player0.getName(), sumForPlayer0);
		points.put(player1.getName(), sumForPlayer1);
		points.put(player2.getName(), sumForPlayer2);
		activePlayerList.get(0).getUltiRoom()
				.sendShowResultMessageToAll(messageHandler, points);

		nextGame();
	}

	private void savePoints(final Player player) {
		playerRepository.updatePoint(player);
	}
}
