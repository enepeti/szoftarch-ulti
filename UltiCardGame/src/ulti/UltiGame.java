package ulti;

import interfaces.dal.IPlayerRepository;
import interfaces.messagers.IMessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import messagers.MessageHandler;
import messagers.util.error.ErrorAnswer;
import messagers.util.ulti.DealAnswer;
import messagers.util.ulti.GameSelectedAnswer;
import messagers.util.ulti.HasToConfirmAnswer;
import messagers.util.ulti.PickUpCardsAnswer;
import messagers.util.ulti.PlayedCardAnswer;
import messagers.util.ulti.PlayerOnTurnAnswer;
import messagers.util.ulti.TakeCardsAnswer;
import ulti.domain.Card;
import ulti.domain.DeckOfCards;
import ulti.domain.SuitType.Suit;
import ulti.domain.UltiPlayer;
import ulti.domain.ValueType.Value;
import ulti.domain.gametype.ConcreteGameType;
import ulti.domain.gametype.GameTypeConverter;
import dal.PlayerRepository;
import domain.ActivePlayer;
import domain.Player;
import domain.PlayerTypeClass.PlayerType;

public class UltiGame {

	private final List<ActivePlayer> activePlayerList;
	private int starterActivePlayer = -1;
	private int activePlayerOnTurn = -1;
	private int lastPlayerWithConcreteGameType = -1;
	private boolean isGameStarted = false;
	private Card remainingCard1 = null;
	private Card remainingCard2 = null;
	private List<Card> cardsOnTable = null;
	private ConcreteGameType concreteGameType = null;
	private DeckOfCards deckOfCards;

	private static GameTypeConverter gameTypeConverter = new GameTypeConverter();

	private final IPlayerRepository playerRepository = new PlayerRepository();
	private final IMessageHandler messageHandler = new MessageHandler();

	public UltiGame(final List<ActivePlayer> activePlayerList) {
		this.activePlayerList = activePlayerList;
		deckOfCards = new DeckOfCards();
		cardsOnTable = new ArrayList<Card>();

		for (final ActivePlayer activePlayerInRoom : activePlayerList) {
			activePlayerInRoom.setUltiPlayer(new UltiPlayer());
		}

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

	private void incrementLastPlayerWithConcreteGameType() {
		if (lastPlayerWithConcreteGameType == 2) {
			lastPlayerWithConcreteGameType = 0;
		} else {
			lastPlayerWithConcreteGameType++;
		}
	}

	private int incrementTakeCards(int takeCards) {
		if (takeCards == 2) {
			takeCards = 0;
		} else {
			takeCards++;
		}

		return takeCards;
	}

	public void deal() {
		deckOfCards.shuffle();

		final List<Card> cards = deckOfCards.getCards();

		final ActivePlayer activePlayer = activePlayerList
				.get(starterActivePlayer);
		final List<Card> subList = cards.subList(0, 12);

		activePlayer.getUltiPlayer().addCardsToHand(subList);
		messageHandler.send(new DealAnswer(subList, true), activePlayer);

		incrementStarterPlayer();
		final ActivePlayer activePlayer2 = activePlayerList
				.get(starterActivePlayer);
		final List<Card> subList2 = cards.subList(12, 22);
		activePlayer2.getUltiPlayer().addCardsToHand(subList2);
		messageHandler.send(new DealAnswer(subList2, false), activePlayer2);

		incrementStarterPlayer();
		final ActivePlayer activePlayer3 = activePlayerList
				.get(starterActivePlayer);
		final List<Card> subList3 = cards.subList(22, 32);
		activePlayer3.getUltiPlayer().addCardsToHand(subList3);
		messageHandler.send(new DealAnswer(subList3, false), activePlayer3);

		incrementStarterPlayer();
	}

	public void say(final ConcreteGameType concreteGameType, final Card card1,
			final Card card2) {
		final ActivePlayer activePlayer = activePlayerList
				.get(activePlayerOnTurn);
		if ((gameTypeConverter
				.convertConcreteGameTypeToInt(this.concreteGameType) >= gameTypeConverter
				.convertConcreteGameTypeToInt(concreteGameType))
				|| (this.concreteGameType == null)) {
			activePlayer.getUltiPlayer().say(card1, card2);
			remainingCard1 = card1;
			remainingCard2 = card2;
			this.concreteGameType = concreteGameType;
			final String name = activePlayer.getPlayer().getName();
			final int convertConcreteGameTypeToInt = gameTypeConverter
					.convertConcreteGameTypeToInt(concreteGameType);
			messageHandler.send(new GameSelectedAnswer(name, true,
					convertConcreteGameTypeToInt), activePlayer);
			activePlayer.getUltiRoom().sendGameSelectionMessageToAllOthers(
					messageHandler, name, convertConcreteGameTypeToInt,
					activePlayer);
			lastPlayerWithConcreteGameType = activePlayerOnTurn;
			nextPlayerTurn();
		} else {
			messageHandler.send(new ErrorAnswer("Nem elég nagy bemondás!"),
					activePlayer);
		}
	}

	public void pass() {
		nextPlayerTurn();
	}

	public void pickUpCards() {
		final List<Card> cardsToHand = new ArrayList<Card>();
		cardsToHand.add(remainingCard1);
		final ActivePlayer activePlayer = activePlayerList
				.get(activePlayerOnTurn);
		activePlayer.getUltiPlayer().addCardsToHand(cardsToHand);
		messageHandler.send(new PickUpCardsAnswer(remainingCard1,
				remainingCard2), activePlayer);
		remainingCard1 = null;
		remainingCard2 = null;
	}

	public void confirm(final Suit trumpSuit) {
		if (concreteGameType.isThereParty() && (trumpSuit != null)) {
			concreteGameType.setTrump(trumpSuit);
		}

		startGame();
	}

	public void nextPlayerTurn() {
		incrementActivePlayerOnTurn();

		final ActivePlayer activePlayer = activePlayerList
				.get(activePlayerOnTurn);
		final String name = activePlayer.getPlayer().getName();
		messageHandler.send(new PlayerOnTurnAnswer(name, true), activePlayer);
		activePlayer.getUltiRoom().sendNextPlayerOnTurnMessageToAllOthers(
				messageHandler, name, activePlayer);

		if ((lastPlayerWithConcreteGameType == activePlayerOnTurn)
				&& !isGameStarted) {
			messageHandler.send(new HasToConfirmAnswer(), activePlayer);
		}
	}

	public void startGame() {
		activePlayerList.get(0).getUltiRoom()
		.sendStartGameMessageToAll(messageHandler);
		isGameStarted = true;

		// TODO: lekérni klienstõl
		concreteGameType.setTrump(Suit.LEAF);

		final ActivePlayer activePlayer = activePlayerList
				.get(activePlayerOnTurn);
		final String name = activePlayer.getPlayer().getName();
		messageHandler.send(new PlayerOnTurnAnswer(name, true), activePlayer);
		activePlayer.getUltiRoom().sendNextPlayerOnTurnMessageToAllOthers(
				messageHandler, name, activePlayer);
	}

	public void playCard(final Card card, final ActivePlayer senderActivePlayer) {
		if (isGameStarted) {
			if (validatePlayer(senderActivePlayer)) {
				if (validateCard(card, senderActivePlayer)) {
					cardsOnTable.add(card);
					final ActivePlayer activePlayer = activePlayerList
							.get(activePlayerOnTurn);
					activePlayer.getUltiPlayer().playCard(card);
					final String name = activePlayer.getPlayer().getName();
					messageHandler.send(new PlayedCardAnswer(name, true, card),
							activePlayer);
					activePlayer.getUltiRoom()
					.sendPlayedCardMessageToAllOthers(messageHandler,
							name, card, activePlayer);
					if (cardsOnTable.size() == 3) {
						evaluateTurn();
					} else {
						nextPlayerTurn();
					}
				} else {
					messageHandler.send(new ErrorAnswer(
							"Nem tehetõ le az a lap"), senderActivePlayer);
				}
			} else {
				messageHandler.send(new ErrorAnswer("Nem Ön jön!"),
						senderActivePlayer);
			}
		} else {
			messageHandler.send(new ErrorAnswer(
					"Még a licitálás tart, nem játszhat ki lapot!"),
					senderActivePlayer);
		}
	}

	private boolean validateCard(final Card card,
			final ActivePlayer senderActivePlayer) {
		final UltiPlayer ultiPlayer = senderActivePlayer.getUltiPlayer();
		if (cardsOnTable.isEmpty()) {
			return true;
		} else {
			if (cardsOnTable.size() == 1) {
				final Card firstAndOnlyCardOnTable = cardsOnTable.get(0);
				final Suit cardOnTableSuit = firstAndOnlyCardOnTable.getSuit();
				final Value cardOnTableValue = firstAndOnlyCardOnTable
						.getValue();
				final Suit cardSuit = card.getSuit();
				final Value cardValue = card.getValue();
				final Suit trumpSuit = concreteGameType.getTrump();

				if (cardSuit.compareTo(cardOnTableSuit) == 0) {
					if (cardValue.ordinal() < cardOnTableValue.ordinal()) {
						return true;
					} else {
						return hasNoBiggerCard(ultiPlayer, cardOnTableSuit,
								cardOnTableValue);
					}
				} else if (cardSuit.compareTo(trumpSuit) == 0) {
					return hasNoSuchSuit(ultiPlayer, cardOnTableSuit);
				} else {
					if (hasNoSuchSuit(ultiPlayer, cardOnTableSuit)
							&& hasNoSuchSuit(ultiPlayer, trumpSuit)) {
						return true;
					} else {
						return false;
					}
				}
			} else {
				if (cardsOnTable.size() == 2) {
					final Card firstCardOnTable = cardsOnTable.get(0);
					final Suit firstCardOnTableSuit = firstCardOnTable
							.getSuit();
					final Value firstCardOnTableValue = firstCardOnTable
							.getValue();
					final Card secondCardOnTable = cardsOnTable.get(1);
					final Suit secondCardOnTableSuit = secondCardOnTable
							.getSuit();
					final Value secondCardOnTableValue = secondCardOnTable
							.getValue();
					final Suit cardSuit = card.getSuit();
					final Value cardValue = card.getValue();
					final Suit trumpSuit = concreteGameType.getTrump();

					if (cardSuit.compareTo(firstCardOnTableSuit) == 0) {
						if (cardValue.ordinal() < firstCardOnTableValue
								.ordinal()) {
							if (cardSuit.compareTo(secondCardOnTableSuit) == 0) {
								if (cardValue.ordinal() < secondCardOnTableValue
										.ordinal()) {
									return true;
								} else {
									return hasNoBiggerCard(ultiPlayer,
											secondCardOnTableSuit,
											secondCardOnTableValue);
								}
							} else {
								return true;
							}
						} else {
							if (cardSuit.compareTo(secondCardOnTableSuit) == 0) {
								if (firstCardOnTableValue.ordinal() < secondCardOnTableValue
										.ordinal()) {
									return hasNoBiggerCard(ultiPlayer,
											firstCardOnTableSuit,
											firstCardOnTableValue);
								} else {
									if (cardValue.ordinal() < secondCardOnTableValue
											.ordinal()) {
										return true;
									} else {
										return hasNoBiggerCard(ultiPlayer,
												secondCardOnTableSuit,
												secondCardOnTableValue);
									}
								}
							} else {
								if (secondCardOnTableSuit.compareTo(trumpSuit) == 0) {
									return true;
								} else {
									return hasNoBiggerCard(ultiPlayer,
											firstCardOnTableSuit,
											firstCardOnTableValue);
								}
							}
						}
					} else if (cardSuit.compareTo(trumpSuit) == 0) {
						if (secondCardOnTableSuit.compareTo(trumpSuit) == 0) {
							if (hasNoSuchSuit(ultiPlayer, firstCardOnTableSuit)) {
								if (cardValue.ordinal() < secondCardOnTableValue
										.ordinal()) {
									return true;
								} else {
									return hasNoBiggerCard(ultiPlayer,
											secondCardOnTableSuit,
											secondCardOnTableValue);
								}
							} else {
								return false;
							}
						} else {
							return hasNoSuchSuit(ultiPlayer,
									firstCardOnTableSuit);
						}
					} else {
						if (hasNoSuchSuit(ultiPlayer, firstCardOnTableSuit)
								&& hasNoSuchSuit(ultiPlayer, trumpSuit)) {
							return true;
						} else {
							return false;
						}
					}
				} else {
					System.out.println("Too many cards on the table");
					return false;
				}
			}
		}
	}

	private boolean hasNoBiggerCard(final UltiPlayer ultiPlayer,
			final Suit cardOnTableSuit, final Value cardOnTableValue) {
		for (final Card cardInHand : ultiPlayer.getHand()) {
			if (cardInHand.getSuit().compareTo(cardOnTableSuit) == 0) {
				if (cardInHand.getValue().ordinal() < cardOnTableValue
						.ordinal()) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean hasNoSuchSuit(final UltiPlayer ultiPlayer,
			final Suit cardOnTableSuit) {
		for (final Card cardInHand : ultiPlayer.getHand()) {
			if (cardInHand.getSuit().compareTo(cardOnTableSuit) == 0) {
				return false;
			}
		}

		return true;
	}

	private boolean validatePlayer(final ActivePlayer senderActivePlayer) {
		if (senderActivePlayer == activePlayerList.get(activePlayerOnTurn)) {
			return true;
		}

		return false;
	}

	private void nextPlayerTurnAfterEvaluation() {
		final ActivePlayer activePlayer = activePlayerList
				.get(activePlayerOnTurn);
		final String name = activePlayer.getPlayer().getName();
		messageHandler.send(new PlayerOnTurnAnswer(name, true), activePlayer);
		activePlayer.getUltiRoom().sendNextPlayerOnTurnMessageToAllOthers(
				messageHandler, activePlayer.getPlayer().getName(),
				activePlayer);
	}

	public void endGame() {

	}

	public void nextGame() {
		concreteGameType = null;
		isGameStarted = false;
		incrementStarterPlayer();
		lastPlayerWithConcreteGameType = -1;
		remainingCard1 = null;
		remainingCard2 = null;
		cardsOnTable = null;
		deckOfCards = new DeckOfCards();
		activePlayerOnTurn = starterActivePlayer;
		deal();
	}

	public void evaluateTurn() {
		int takeCards = determineWhoTakesCards();
		if (activePlayerOnTurn == 0) {
			takeCards = incrementTakeCards(takeCards);
		} else if (activePlayerOnTurn == 1) {
			takeCards = incrementTakeCards(takeCards);
			takeCards = incrementTakeCards(takeCards);
		}
		final ActivePlayer activePlayer = activePlayerList.get(takeCards);
		activePlayer.getUltiPlayer().addCardsToTaken(cardsOnTable);
		final String name = activePlayer.getPlayer().getName();
		messageHandler.send(new TakeCardsAnswer(name, true, cardsOnTable),
				activePlayer);
		activePlayer.getUltiRoom().sendTakenCardsMessageToAllOthers(
				messageHandler, name, cardsOnTable, activePlayer);
		cardsOnTable = new ArrayList<Card>();

		if (activePlayer.getUltiPlayer().getHand().isEmpty()) {
			evaluateGame(activePlayer);
		} else {
			activePlayerOnTurn = takeCards;
			nextPlayerTurnAfterEvaluation();
		}
	}

	private int determineWhoTakesCards() {
		final HashMap<Card, Integer> orderMap = createCardOrder(concreteGameType
				.getTrump());

		return takeCardIndex(orderMap);
	}

	private int takeCardIndex(final HashMap<Card, Integer> orderMap) {
		int first = 0;
		int second = 0;
		int third = 0;
		final Suit suit1 = cardsOnTable.get(0).getSuit();
		final Value value1 = cardsOnTable.get(0).getValue();
		final Suit suit2 = cardsOnTable.get(1).getSuit();
		final Value value2 = cardsOnTable.get(1).getValue();
		final Suit suit3 = cardsOnTable.get(2).getSuit();
		final Value value3 = cardsOnTable.get(2).getValue();
		for (final Entry<Card, Integer> entry : orderMap.entrySet()) {
			if ((entry.getKey().getSuit().compareTo(suit1) == 0)
					&& (entry.getKey().getValue().compareTo(value1) == 0)) {
				first = entry.getValue();
			}

			if ((entry.getKey().getSuit().compareTo(suit2) == 0)
					&& (entry.getKey().getValue().compareTo(value2) == 0)) {
				second = entry.getValue();
			}

			if ((entry.getKey().getSuit().compareTo(suit3) == 0)
					&& (entry.getKey().getValue().compareTo(value3) == 0)) {
				third = entry.getValue();
			}
		}

		return findMin(first, second, third);
	}

	private int findMin(final Integer firstOrder, final Integer secondOrder,
			final Integer thirdOrder) {
		int min = firstOrder;
		int minIndex = 0;

		if (secondOrder < min) {
			min = secondOrder;
			minIndex = 1;
		}

		if (thirdOrder < min) {
			min = thirdOrder;
			minIndex = 2;
		}

		return minIndex;
	}

	private HashMap<Card, Integer> createCardOrder(final Suit trumpSuit) {
		final HashMap<Card, Integer> orderMap = new HashMap<Card, Integer>();

		int orderNumber = 1;
		for (final Value value : Value.values()) {
			orderMap.put(new Card(trumpSuit, value), orderNumber++);
		}

		final Suit firstCardSuit = cardsOnTable.get(0).getSuit();
		if (firstCardSuit.compareTo(trumpSuit) != 0) {
			for (final Value value : Value.values()) {
				orderMap.put(new Card(firstCardSuit, value), orderNumber++);
			}
		}

		orderNumber++;
		for (final Suit suit : Suit.values()) {
			if ((suit.compareTo(firstCardSuit) != 0)
					&& (suit.compareTo(trumpSuit) != 0)) {
				for (final Value value : Value.values()) {
					orderMap.put(new Card(suit, value), orderNumber);
				}
			}
		}

		return orderMap;
	}

	private void evaluateGame(final ActivePlayer activePlayer) {
		// TODO: játék pontok számolása
		int sumForPlayerWithSaying = 0;
		int sumForOpponent1 = 0;
		int sumForOpponent2 = 0;

		incrementLastPlayerWithConcreteGameType();
		final int indexOfOpponent1 = lastPlayerWithConcreteGameType;
		incrementLastPlayerWithConcreteGameType();
		final int indexOfOpponent2 = lastPlayerWithConcreteGameType;
		incrementLastPlayerWithConcreteGameType();

		if (concreteGameType.isThereParty()) {
			int sumForPlayer = 0;
			int sumForOpponent1Party = 0;
			int sumForOpponent2Party = 0;
			for (int i = 0; i < 3; i++) {
				final int sum = activePlayerList.get(i).getUltiPlayer()
						.calculateParty();
				if (i == lastPlayerWithConcreteGameType) {
					sumForPlayer += sum;
				} else if (i == indexOfOpponent1) {
					sumForOpponent1Party += sum;
				} else if (i == indexOfOpponent2) {
					sumForOpponent2Party += sum;
				}
			}

			if (activePlayer == activePlayerList
					.get(lastPlayerWithConcreteGameType)) {
				sumForPlayer += 10;
			} else if (activePlayer == activePlayerList.get(indexOfOpponent1)) {
				sumForOpponent1Party += 10;
			} else if (activePlayer == activePlayerList.get(indexOfOpponent2)) {
				sumForOpponent2Party += 10;
			}

			final HashMap<String, Integer> partyPoints = new HashMap<String, Integer>();
			partyPoints.put(activePlayerList
					.get(lastPlayerWithConcreteGameType).getPlayer().getName(),
					sumForPlayer);
			partyPoints.put(activePlayerList.get(indexOfOpponent1).getPlayer()
					.getName(), sumForOpponent1Party);
			partyPoints.put(activePlayerList.get(indexOfOpponent2).getPlayer()
					.getName(), sumForOpponent2Party);
			activePlayerList
					.get(0)
					.getUltiRoom()
			.sendShowPartyResultMessageToAll(messageHandler,
							partyPoints);

			if ((sumForOpponent1Party + sumForOpponent2Party) < sumForPlayer) {
				sumForPlayerWithSaying += 2;
				sumForOpponent1 -= 1;
				sumForOpponent2 -= 1;
			} else {
				sumForPlayerWithSaying -= 2;
				sumForOpponent1 += 1;
				sumForOpponent2 += 1;
			}
		}

		final Player playerWithSaying = activePlayerList.get(
				lastPlayerWithConcreteGameType).getPlayer();
		playerWithSaying.addPoint(sumForPlayerWithSaying);
		if (playerWithSaying.getType() != PlayerType.GUEST) {
			savePoints(playerWithSaying);
		}
		incrementLastPlayerWithConcreteGameType();

		final Player player1 = activePlayerList.get(
				lastPlayerWithConcreteGameType).getPlayer();
		player1.addPoint(sumForOpponent1);
		if (player1.getType() != PlayerType.GUEST) {
			savePoints(player1);
		}
		incrementLastPlayerWithConcreteGameType();

		final Player player2 = activePlayerList.get(
				lastPlayerWithConcreteGameType).getPlayer();
		player2.addPoint(sumForOpponent2);
		if (player2.getType() != PlayerType.GUEST) {
			savePoints(player2);
		}

		final HashMap<String, Integer> points = new HashMap<String, Integer>();
		points.put(playerWithSaying.getName(), sumForPlayerWithSaying);
		points.put(player1.getName(), sumForOpponent1);
		points.put(player2.getName(), sumForOpponent2);
		activePlayerList.get(0).getUltiRoom()
		.sendShowResultMessageToAll(messageHandler, points);

		nextGame();
	}

	private void savePoints(final Player player) {
		playerRepository.updatePoint(player);
	}
}
