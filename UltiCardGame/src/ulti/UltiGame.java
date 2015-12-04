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

	private boolean didPlayerHaveFortyInFortyHundredGame = true;
	private boolean didPlayerHaveTwentyInTwentyHundredGame = true;
	private boolean didPlayerHaveTrumpSevenInUltiGame = true;
	private boolean didPlayerTakeCardInBetliGame = false;
	private boolean didPlayerNotTakeCardInDurchmarsGame = false;

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

		final String starterName = activePlayerList.get(starterActivePlayer)
				.getPlayer().getName();

		activePlayer.getUltiPlayer().addCardsToHand(subList);
		messageHandler.send(new DealAnswer(subList, true, starterName),
				activePlayer);

		incrementStarterPlayer();
		final ActivePlayer activePlayer2 = activePlayerList
				.get(starterActivePlayer);
		final List<Card> subList2 = cards.subList(12, 22);
		activePlayer2.getUltiPlayer().addCardsToHand(subList2);
		messageHandler.send(new DealAnswer(subList2, false, starterName),
				activePlayer2);

		incrementStarterPlayer();
		final ActivePlayer activePlayer3 = activePlayerList
				.get(starterActivePlayer);
		final List<Card> subList3 = cards.subList(22, 32);
		activePlayer3.getUltiPlayer().addCardsToHand(subList3);
		messageHandler.send(new DealAnswer(subList3, false, starterName),
				activePlayer3);

		incrementStarterPlayer();
	}

	public void say(final ConcreteGameType concreteGameType, final Card card1,
			final Card card2) {
		final ActivePlayer activePlayer = activePlayerList
				.get(activePlayerOnTurn);
		if ((this.concreteGameType == null)
				|| (this.concreteGameType.getWholeValue() < concreteGameType
						.getWholeValue())
				|| ((this.concreteGameType.getWholeValue() == concreteGameType
						.getWholeValue()) && (concreteGameType
								.getGameTypeList().size() < this.concreteGameType
								.getGameTypeList().size()))) {
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

	public void pickUpCards(final ActivePlayer senderActivePlayer) {
		if (validatePlayer(senderActivePlayer)) {
			if (!isGameStarted) {
				final List<Card> cardsToHand = new ArrayList<Card>();
				cardsToHand.add(remainingCard1);
				cardsToHand.add(remainingCard2);
				senderActivePlayer.getUltiPlayer().addCardsToHand(cardsToHand);
				messageHandler.send(new PickUpCardsAnswer(remainingCard1,
						remainingCard2), senderActivePlayer);
				remainingCard1 = null;
				remainingCard2 = null;
			} else {
				messageHandler.send(new ErrorAnswer(
						"Már a játék tart, nem vehet fel lapot!"),
						senderActivePlayer);
			}
		} else {
			messageHandler.send(new ErrorAnswer("Nem Ön jön!"),
					senderActivePlayer);
		}
	}

	public void confirm(final Suit trumpSuit, final ActivePlayer activePlayer) {
		if (activePlayer == activePlayerList
				.get(lastPlayerWithConcreteGameType)) {
			if (concreteGameType.isThereTrump() && (trumpSuit != null)) {
				if (!concreteGameType.isItRed()) {
					if (trumpSuit.compareTo(Suit.HEART) != 0) {
						concreteGameType.setTrump(trumpSuit);
					}
				}
			}

			startGame();
		} else {
			messageHandler
					.send(new ErrorAnswer(
							"Nem tudsz játékot jóváhagyni, mert nem te mondtál be utoljára!"),
							activePlayer);
		}
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
		boolean startGame = true;
		boolean countTwentiesAndForty = true;
		final ActivePlayer activePlayerWhoIsOnTurn = activePlayerList
				.get(activePlayerOnTurn);
		final HashMap<String, Integer> points = new HashMap<String, Integer>();

		if (concreteGameType.isThereActualGameType("40-100")) {
			countTwentiesAndForty = false;
			if (!activePlayerWhoIsOnTurn.getUltiPlayer().isThereForty(
					concreteGameType.getTrump())) {
				didPlayerHaveFortyInFortyHundredGame = false;
				activePlayerWhoIsOnTurn.getUltiRoom()
						.sendDoesNotHaveFortyMessageToAll(messageHandler);
				if (concreteGameType.getGameTypeList().size() == 1) {
					startGame = false;
					evaluateGame(activePlayerWhoIsOnTurn);
				}
			}
		}

		if (startGame) {
			if (concreteGameType.isThereActualGameType("20-100")) {
				countTwentiesAndForty = false;
				if (!activePlayerWhoIsOnTurn.getUltiPlayer().isThereTwenty(
						concreteGameType.getTrump())) {
					didPlayerHaveTwentyInTwentyHundredGame = false;
					activePlayerWhoIsOnTurn.getUltiRoom()
							.sendDoesNotHaveTwentyMessageToAll(messageHandler);
					if (concreteGameType.getGameTypeList().size() == 1) {
						startGame = false;
						evaluateGame(activePlayerWhoIsOnTurn);
					}
				}
			}
		}

		if (startGame) {
			if (concreteGameType.isThereActualGameType("Betli")
					|| concreteGameType.isThereActualGameType("Durchmars")) {
				countTwentiesAndForty = false;
			}

			if (countTwentiesAndForty) {
				for (final ActivePlayer activePlayer : activePlayerList) {
					final int sum = activePlayer.getUltiPlayer()
							.countTwentysAndFortys(concreteGameType.getTrump());
					points.put(activePlayer.getPlayer().getName(), sum);
				}
			}

			if (concreteGameType.isThereActualGameType("Ulti")) {
				if (!activePlayerWhoIsOnTurn.getUltiPlayer().isThereTrumpSeven(
						concreteGameType.getTrump())) {
					didPlayerHaveTrumpSevenInUltiGame = false;
					activePlayerWhoIsOnTurn.getUltiRoom()
							.sendDoesNotHaveTrumpSevenMessageToAll(
									messageHandler);
				}
			}

			final Suit trump = concreteGameType.getTrump();
			String trumpToGiveBack = null;
			if (trump != null) {
				trumpToGiveBack = trump.toString();
			}

			activePlayerList
					.get(0)
					.getUltiRoom()
					.sendStartGameMessageToAll(messageHandler, points,
							trumpToGiveBack);

			isGameStarted = true;

			final String name = activePlayerWhoIsOnTurn.getPlayer().getName();
			messageHandler.send(new PlayerOnTurnAnswer(name, true),
					activePlayerWhoIsOnTurn);
			activePlayerWhoIsOnTurn.getUltiRoom()
					.sendNextPlayerOnTurnMessageToAllOthers(messageHandler,
							name, activePlayerWhoIsOnTurn);
		}
	}

	public void playCard(final Card card, final ActivePlayer senderActivePlayer) {
		if (isGameStarted) {
			if (validatePlayer(senderActivePlayer)) {
				if (validateCard(card, senderActivePlayer)) {
					cardsOnTable.add(card);
					final ActivePlayer activePlayer = activePlayerList
							.get(activePlayerOnTurn);
					activePlayer.getUltiPlayer().playCard(card,
							concreteGameType.getTrump(),
							concreteGameType.isThereActualGameType("Ulti"));
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
		final boolean isThereTrump = concreteGameType.isThereTrump();
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
					if (cardValue.getVal(concreteGameType.isTenUp()) < cardOnTableValue
							.getVal(concreteGameType.isTenUp())) {
						return true;
					} else {
						return hasNoBiggerCard(ultiPlayer, cardOnTableSuit,
								cardOnTableValue);
					}
				} else {
					if (isThereTrump && (cardSuit.compareTo(trumpSuit) == 0)) {
						return hasNoSuchSuit(ultiPlayer, cardOnTableSuit);
					} else if (isThereTrump) {
						if (hasNoSuchSuit(ultiPlayer, cardOnTableSuit)
								&& hasNoSuchSuit(ultiPlayer, trumpSuit)) {
							return true;
						} else {
							return false;
						}
					} else {
						if (hasNoSuchSuit(ultiPlayer, cardOnTableSuit)) {
							return true;
						} else {
							return false;
						}
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
						if (cardValue.getVal(concreteGameType.isTenUp()) < firstCardOnTableValue
								.getVal(concreteGameType.isTenUp())) {
							if (cardSuit.compareTo(secondCardOnTableSuit) == 0) {
								if (cardValue
										.getVal(concreteGameType.isTenUp()) < secondCardOnTableValue
										.getVal(concreteGameType.isTenUp())) {
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
								if (firstCardOnTableValue
										.getVal(concreteGameType.isTenUp()) < secondCardOnTableValue
										.getVal(concreteGameType.isTenUp())) {
									return hasNoBiggerCard(ultiPlayer,
											firstCardOnTableSuit,
											firstCardOnTableValue);
								} else {
									if (cardValue.getVal(concreteGameType
											.isTenUp()) < secondCardOnTableValue
											.getVal(concreteGameType.isTenUp())) {
										return true;
									} else {
										return hasNoBiggerCard(ultiPlayer,
												secondCardOnTableSuit,
												secondCardOnTableValue);
									}
								}
							} else {
								if (isThereTrump) {
									if (secondCardOnTableSuit
											.compareTo(trumpSuit) == 0) {
										return true;
									} else {
										return hasNoBiggerCard(ultiPlayer,
												firstCardOnTableSuit,
												firstCardOnTableValue);
									}
								} else {
									return hasNoBiggerCard(ultiPlayer,
											firstCardOnTableSuit,
											firstCardOnTableValue);
								}
							}
						}
					} else if (isThereTrump
							&& (cardSuit.compareTo(trumpSuit) == 0)) {
						if (secondCardOnTableSuit.compareTo(trumpSuit) == 0) {
							if (hasNoSuchSuit(ultiPlayer, firstCardOnTableSuit)) {
								if (cardValue
										.getVal(concreteGameType.isTenUp()) < secondCardOnTableValue
										.getVal(concreteGameType.isTenUp())) {
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
						if (isThereTrump) {
							if (hasNoSuchSuit(ultiPlayer, firstCardOnTableSuit)
									&& hasNoSuchSuit(ultiPlayer, trumpSuit)) {
								return true;
							} else {
								return false;
							}
						} else {
							if (hasNoSuchSuit(ultiPlayer, firstCardOnTableSuit)) {
								return true;
							} else {
								return false;
							}
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
				if (cardInHand.getValue().getVal(concreteGameType.isTenUp()) < cardOnTableValue
						.getVal(concreteGameType.isTenUp())) {
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
		for (final ActivePlayer activePlayer : activePlayerList) {
			activePlayer.getUltiPlayer().setSumForTwentysAndFortys(0);
			activePlayer.getUltiPlayer().setLastCardTrumpSeven(false);
		}

		concreteGameType = null;
		isGameStarted = false;
		incrementStarterPlayer();
		lastPlayerWithConcreteGameType = -1;
		remainingCard1 = null;
		remainingCard2 = null;
		deckOfCards = new DeckOfCards();
		cardsOnTable = new ArrayList<Card>();
		activePlayerOnTurn = starterActivePlayer;
		didPlayerHaveFortyInFortyHundredGame = true;
		didPlayerHaveTwentyInTwentyHundredGame = true;
		didPlayerHaveTrumpSevenInUltiGame = true;
		didPlayerTakeCardInBetliGame = false;
		didPlayerNotTakeCardInDurchmarsGame = false;
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

		final ActivePlayer activePlayerWhoTookCards = activePlayerList
				.get(takeCards);
		activePlayerWhoTookCards.getUltiPlayer().addCardsToTaken(cardsOnTable);
		final String name = activePlayerWhoTookCards.getPlayer().getName();
		messageHandler.send(new TakeCardsAnswer(name, true, cardsOnTable),
				activePlayerWhoTookCards);
		activePlayerWhoTookCards.getUltiRoom()
				.sendTakenCardsMessageToAllOthers(messageHandler, name,
						cardsOnTable, activePlayerWhoTookCards);
		cardsOnTable = new ArrayList<Card>();

		boolean continuePlaying = true;
		if (concreteGameType.isThereActualGameType("Betli")) {
			if (activePlayerWhoTookCards == activePlayerList
					.get(lastPlayerWithConcreteGameType)) {
				didPlayerTakeCardInBetliGame = true;
				evaluateGame(activePlayerWhoTookCards);
				continuePlaying = false;
			}
		}

		if (continuePlaying) {
			if (concreteGameType.isThereActualGameType("Durchmars")) {
				if (activePlayerWhoTookCards != activePlayerList
						.get(lastPlayerWithConcreteGameType)) {
					didPlayerNotTakeCardInDurchmarsGame = true;
					if (concreteGameType.getGameTypeList().size() == 1) {
						evaluateGame(activePlayerWhoTookCards);
						continuePlaying = false;
					}
				}
			}
		}

		if (continuePlaying) {
			if (activePlayerWhoTookCards.getUltiPlayer().getHand().isEmpty()) {
				evaluateGame(activePlayerWhoTookCards);
			} else {
				activePlayerOnTurn = takeCards;
				nextPlayerTurnAfterEvaluation();
			}
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
		final Suit firstCardSuit = cardsOnTable.get(0).getSuit();
		int orderNumber = 1;
		final boolean tenUp = concreteGameType.isTenUp();

		if (tenUp) {
			for (final Value value : Value.values()) {
				orderMap.put(new Card(trumpSuit, value), orderNumber++);
			}

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
		} else {
			for (final Value value : Value.ACE.getValues(tenUp)) {
				orderMap.put(new Card(firstCardSuit, value), orderNumber++);
			}

			orderNumber++;
			for (final Suit suit : Suit.values()) {
				if ((suit.compareTo(firstCardSuit) != 0)) {
					for (final Value value : Value.values()) {
						orderMap.put(new Card(suit, value), orderNumber);
					}
				}
			}
		}

		return orderMap;
	}

	private void evaluateGame(final ActivePlayer activePlayerWhoTookCards) {
		int sumForPlayerWithSaying = 0;
		int sumForOpponent1 = 0;
		int sumForOpponent2 = 0;

		incrementLastPlayerWithConcreteGameType();
		final int indexOfOpponent1 = lastPlayerWithConcreteGameType;
		incrementLastPlayerWithConcreteGameType();
		final int indexOfOpponent2 = lastPlayerWithConcreteGameType;
		incrementLastPlayerWithConcreteGameType();

		final ActivePlayer activePlayerSayer = activePlayerList
				.get(lastPlayerWithConcreteGameType);
		final ActivePlayer activePlayerOpponent1 = activePlayerList
				.get(indexOfOpponent1);
		final ActivePlayer activePlayerOpponent2 = activePlayerList
				.get(indexOfOpponent2);

		if (concreteGameType.isThereActualGameType("40-100")) {
			boolean winSayer;
			if (!didPlayerHaveFortyInFortyHundredGame) {
				winSayer = false;
			} else {
				int sum = 0;
				if (activePlayerWhoTookCards == activePlayerSayer) {
					sum += 10;
				}
				if ((activePlayerList.get(lastPlayerWithConcreteGameType)
						.getUltiPlayer().calculateParty()
						+ sum + 40) >= 100) {
					winSayer = true;
				} else {
					winSayer = false;
				}
			}

			int red = 1;
			if (concreteGameType.isItRed()) {
				red = 2;
			}
			if (winSayer) {
				sumForPlayerWithSaying += red * 2 * 4;
				sumForOpponent1 -= red * 4;
				sumForOpponent2 -= red * 4;
			} else {
				sumForPlayerWithSaying -= red * 2 * 4;
				sumForOpponent1 += red * 4;
				sumForOpponent2 += red * 4;
			}
		}

		if (concreteGameType.isThereActualGameType("20-100")) {
			boolean winSayer;
			if (!didPlayerHaveTwentyInTwentyHundredGame) {
				winSayer = false;
			} else {
				int sum = 0;
				if (activePlayerWhoTookCards == activePlayerSayer) {
					sum += 10;
				}
				if ((activePlayerList.get(lastPlayerWithConcreteGameType)
						.getUltiPlayer().calculateParty()
						+ sum + 20) >= 100) {
					winSayer = true;
				} else {
					winSayer = false;
				}
			}

			int red = 1;
			if (concreteGameType.isItRed()) {
				red = 2;
			}
			if (winSayer) {
				sumForPlayerWithSaying += red * 2 * 8;
				sumForOpponent1 -= red * 8;
				sumForOpponent2 -= red * 8;
			} else {
				sumForPlayerWithSaying -= red * 2 * 8;
				sumForOpponent1 += red * 8;
				sumForOpponent2 += red * 8;
			}
		}

		if (concreteGameType.isThereActualGameType("Ulti")) {
			boolean winSayer;
			if (!didPlayerHaveTrumpSevenInUltiGame) {
				winSayer = false;
			} else {
				if (activePlayerWhoTookCards != activePlayerSayer) {
					winSayer = false;
				} else if (activePlayerSayer.getUltiPlayer()
						.isLastCardTrumpSeven()) {
					winSayer = true;
				} else {
					winSayer = false;
				}
			}

			int red = 1;
			if (concreteGameType.isItRed()) {
				red = 2;
			}
			if (winSayer) {
				sumForPlayerWithSaying += red * 2 * 4;
				sumForOpponent1 -= red * 4;
				sumForOpponent2 -= red * 4;
			} else {
				sumForPlayerWithSaying -= red * 4 * 4;
				sumForOpponent1 += red * 2 * 4;
				sumForOpponent2 += red * 2 * 4;
			}
		}

		if (concreteGameType.isThereActualGameType("Betli")) {
			if (didPlayerTakeCardInBetliGame) {
				sumForPlayerWithSaying -= 2 * concreteGameType.getValue();
				sumForOpponent1 += concreteGameType.getValue();
				sumForOpponent2 += concreteGameType.getValue();
			} else {
				sumForPlayerWithSaying += 2 * concreteGameType.getValue();
				sumForOpponent1 -= concreteGameType.getValue();
				sumForOpponent2 -= concreteGameType.getValue();
			}
		}

		if (concreteGameType.isThereActualGameType("Durchmars")) {
			int red = 1;
			if (concreteGameType.isItRed()) {
				red = 2;
			}
			if (didPlayerNotTakeCardInDurchmarsGame) {
				sumForPlayerWithSaying -= red * 2 * 6;
				sumForOpponent1 += red * 6;
				sumForOpponent2 += red * 6;
			} else {
				sumForPlayerWithSaying += red * 2 * 6;
				sumForOpponent1 -= red * 6;
				sumForOpponent2 -= red * 6;
			}
		}

		if (concreteGameType.isThereParty()
				|| concreteGameType.isThereActualGameType("40-100")
				|| concreteGameType.isThereActualGameType("20-100")) {
			int sumForPlayerParty = 0;
			int sumForOpponent1Party = 0;
			int sumForOpponent2Party = 0;
			for (int i = 0; i < 3; i++) {
				final int sum = activePlayerList.get(i).getUltiPlayer()
						.calculateParty();
				if (i == lastPlayerWithConcreteGameType) {
					sumForPlayerParty += sum;
				} else if (i == indexOfOpponent1) {
					sumForOpponent1Party += sum;
				} else if (i == indexOfOpponent2) {
					sumForOpponent2Party += sum;
				}
			}

			if (activePlayerWhoTookCards == activePlayerSayer) {
				sumForPlayerParty += 10;
			} else if (activePlayerWhoTookCards == activePlayerOpponent1) {
				sumForOpponent1Party += 10;
			} else if (activePlayerWhoTookCards == activePlayerOpponent2) {
				sumForOpponent2Party += 10;
			}

			sumForPlayerParty += activePlayerSayer.getUltiPlayer()
					.getSumForTwentysAndFortys();
			sumForOpponent1Party += activePlayerOpponent1.getUltiPlayer()
					.getSumForTwentysAndFortys();
			sumForOpponent2Party += activePlayerOpponent2.getUltiPlayer()
					.getSumForTwentysAndFortys();

			final HashMap<String, Integer> partyPoints = new HashMap<String, Integer>();
			partyPoints.put(activePlayerSayer.getPlayer().getName(),
					sumForPlayerParty);
			partyPoints.put(activePlayerOpponent1.getPlayer().getName(),
					sumForOpponent1Party);
			partyPoints.put(activePlayerOpponent2.getPlayer().getName(),
					sumForOpponent2Party);
			activePlayerList
					.get(0)
					.getUltiRoom()
					.sendShowPartyResultMessageToAll(messageHandler,
							partyPoints);

			final int partyValue = concreteGameType.getPartyValue();
			if ((sumForOpponent1Party + sumForOpponent2Party) < sumForPlayerParty) {
				sumForPlayerWithSaying += 2 * partyValue;
				sumForOpponent1 -= partyValue;
				sumForOpponent2 -= partyValue;
			} else {
				sumForPlayerWithSaying -= 2 * partyValue;
				sumForOpponent1 += partyValue;
				sumForOpponent2 += partyValue;
			}
		}

		final Player playerWithSaying = activePlayerSayer.getPlayer();
		playerWithSaying.addPoint(sumForPlayerWithSaying);
		if (playerWithSaying.getType() != PlayerType.GUEST) {
			savePoints(playerWithSaying);
		}

		final Player playerOpponent1 = activePlayerOpponent1.getPlayer();
		playerOpponent1.addPoint(sumForOpponent1);
		if (playerOpponent1.getType() != PlayerType.GUEST) {
			savePoints(playerOpponent1);
		}

		final Player playerOpponent2 = activePlayerOpponent2.getPlayer();
		playerOpponent2.addPoint(sumForOpponent2);
		if (playerOpponent2.getType() != PlayerType.GUEST) {
			savePoints(playerOpponent2);
		}

		final HashMap<String, Integer> points = new HashMap<String, Integer>();
		points.put(playerWithSaying.getName(), sumForPlayerWithSaying);
		points.put(playerOpponent1.getName(), sumForOpponent1);
		points.put(playerOpponent2.getName(), sumForOpponent2);
		activePlayerList.get(0).getUltiRoom()
				.sendShowResultMessageToAll(messageHandler, points);

		nextGame();
	}

	private void savePoints(final Player player) {
		playerRepository.updatePoint(player);
	}
}
