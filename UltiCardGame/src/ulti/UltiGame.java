package ulti;

import interfaces.IPlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import messagers.util.MessageType.Type;
import ulti.domain.Card;
import ulti.domain.DeckOfCards;
import ulti.domain.gametype.ConcreteGameType;
import dal.PlayerRepository;
import domain.ActivePlayer;
import domain.Player;

public class UltiGame {

	private final ArrayList<ActivePlayer> activePlayerList;
	private int starterActivePlayer;
	private int activePlayerOnTurn;
	private int lastPlayerWithConcreteGameType;
	private Card remainingCard1;
	private Card remainingCard2;
	private ArrayList<Card> cardsOnTable;
	private ConcreteGameType concreteGameType;
	private DeckOfCards deckOfCards;

	private final IPlayerRepository playerRepository = new PlayerRepository();

	public UltiGame(final ArrayList<ActivePlayer> activePlayerList,
			final int starterActivePlayer, final int activePlayerOnTurn,
			final int lastPlayerWithConcreteGameType,
			final Card remainingCard1, final Card remainingCard2,
			final ConcreteGameType concreteGameType,
			final DeckOfCards deckOfCards) {
		super();
		this.activePlayerList = activePlayerList;
		this.starterActivePlayer = starterActivePlayer;
		this.activePlayerOnTurn = activePlayerOnTurn;
		this.lastPlayerWithConcreteGameType = lastPlayerWithConcreteGameType;
		this.remainingCard1 = remainingCard1;
		this.remainingCard2 = remainingCard2;
		this.concreteGameType = concreteGameType;
		this.deckOfCards = deckOfCards;

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

	public ArrayList<ActivePlayer> getActivePlayerList() {
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

		activePlayerList.get(starterActivePlayer).getUltiPlayer()
				.addCardsToHand(cards.subList(0, 11));
		incrementStarterPlayer();
		activePlayerList.get(starterActivePlayer).getUltiPlayer()
				.addCardsToHand(cards.subList(12, 21));
		incrementStarterPlayer();
		activePlayerList.get(starterActivePlayer).getUltiPlayer()
				.addCardsToHand(cards.subList(22, 31));
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

	public void pickUpCards(final Card card1, final Card card2) {
		remainingCard1 = null;
		remainingCard2 = null;
	}

	public void confirm() {
		startGame();
	}

	public void nextPlayerTurn() {
		incrementActivePlayerOnTurn();
		if (lastPlayerWithConcreteGameType == activePlayerOnTurn) {
			// TODO: bemondás után volt 2 passz és visszaért hozzá, ilyenkor nem
			// passzolhat, csak jóváhagyhatja a játékot vagy felveheti újra
		}
	}

	public void startGame() {

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
		activePlayerList.get(x).getUltiPlayer().addCardsToTaken(cardsOnTable);

		if (activePlayerList.get(x).getUltiPlayer().getHand().isEmpty()) {
			evaluateGame();
		} else {
			activePlayerOnTurn = x;
			nextPlayerTurnAfterEvaluation();
		}
	}

	private void evaluateGame() {
		// TODO: játék pontok számolása
		int sumForPlayer1 = 0;
		int sumForPlayer2 = 0;
		int sumForPlayer3 = 0;

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

			sumForPlayer1 += sumForPlayer;
			sumForPlayer2 += sumForOpponents;
			sumForPlayer3 += sumForOpponents;
		}

		final Player player1 = activePlayerList.get(0).getPlayer();
		player1.addPoint(sumForPlayer1);
		if (player1.getType().toString().toUpperCase() != Type.GUESTLOGIN
				.toString()) {
			savePoints(player1);
		}
		final Player player2 = activePlayerList.get(1).getPlayer();
		player2.addPoint(sumForPlayer2);
		if (player2.getType().toString().toUpperCase() != Type.GUESTLOGIN
				.toString()) {
			savePoints(player2);
		}
		final Player player3 = activePlayerList.get(2).getPlayer();
		player3.addPoint(sumForPlayer3);
		if (player3.getType().toString().toUpperCase() != Type.GUESTLOGIN
				.toString()) {
			savePoints(player3);
		}

		nextGame();
	}

	private void savePoints(final Player player) {
		playerRepository.updatePoint(player);
	}
}
