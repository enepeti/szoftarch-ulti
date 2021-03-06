package ulti.domain;

import java.util.ArrayList;
import java.util.List;

import ulti.domain.SuitType.Suit;
import ulti.domain.ValueType.Value;

public class UltiPlayer {

	private final List<Card> hand = new ArrayList<Card>();
	private int pointsInGamesInActualRoom = 0;
	private List<Card> taken = new ArrayList<Card>();
	private int sumForTwentysAndFortys = 0;
	private boolean isLastCardTrumpSeven = false;;

	public List<Card> getHand() {
		return hand;
	}

	public void setTaken(final List<Card> taken) {
		this.taken = taken;
	}

	public int getPointsInGamesInActualRoom() {
		return pointsInGamesInActualRoom;
	}

	public void setPointsInGamesInActualRoom(final int pointsInGamesInActualRoom) {
		this.pointsInGamesInActualRoom = pointsInGamesInActualRoom;
	}

	public int getSumForTwentysAndFortys() {
		return sumForTwentysAndFortys;
	}

	public void setSumForTwentysAndFortys(final int sumForTwentysAndFortys) {
		this.sumForTwentysAndFortys = sumForTwentysAndFortys;
	}

	public boolean isLastCardTrumpSeven() {
		return isLastCardTrumpSeven;
	}

	public void setLastCardTrumpSeven(final boolean isLastCardTrumpSeven) {
		this.isLastCardTrumpSeven = isLastCardTrumpSeven;
	}

	public void addCardsToHand(final List<Card> cardsToHand) {
		hand.addAll(cardsToHand);
	}

	public void addCardsToTaken(final List<Card> cardsToTaken) {
		taken.addAll(cardsToTaken);
	}

	public void say(final Card card1, final Card card2) {
		final ArrayList<Card> cardsToDrop = new ArrayList<Card>();
		for (final Card cardInHand : hand) {
			if ((cardInHand.getSuit().compareTo(card1.getSuit()) == 0)
					&& (cardInHand.getValue().compareTo(card1.getValue()) == 0)) {
				cardsToDrop.add(cardInHand);
			}

			if ((cardInHand.getSuit().compareTo(card2.getSuit()) == 0)
					&& (cardInHand.getValue().compareTo(card2.getValue()) == 0)) {
				cardsToDrop.add(cardInHand);
			}
		}

		for (final Card cardInHand : cardsToDrop) {
			hand.remove(cardInHand);
		}
	}

	public void pass() {

	}

	public void playCard(final Card card, final Suit trumpSuit,
			final boolean IsThereUlti) {
		for (final Card cardInHand : hand) {
			if ((cardInHand.getSuit().compareTo(card.getSuit()) == 0)
					&& (cardInHand.getValue().compareTo(card.getValue()) == 0)) {
				hand.remove(cardInHand);
				if (IsThereUlti && hand.isEmpty()) {
					if ((card.getSuit().compareTo(trumpSuit) == 0)
							&& (card.getValue().compareTo(Value.SEVEN) == 0)) {
						setLastCardTrumpSeven(true);
					}
				}

				return;
			}
		}
	}

	public void takeCards(final Card card1, final Card card2, final Card card3) {
		taken.add(card1);
		taken.add(card2);
		taken.add(card3);
	}

	public int calculateParty() {
		int sum = 0;
		for (final Card card : taken) {
			final Value value = card.getValue();
			if ((value == Value.ACE) || (value == Value.TEN)) {
				sum += 10;
			}
		}

		return sum;
	}

	public int countTwentysAndFortys(final Suit trump) {
		for (int i = 0; i < 10; i++) {
			for (int j = i + 1; j < 10; j++) {
				final Card card1 = hand.get(i);
				if (card1.getValue().compareTo(Value.OVER_KNAVE) == 0) {
					final Card card2 = hand.get(j);
					if (card2.getValue().compareTo(Value.KING) == 0) {
						if (card1.getSuit().compareTo(card2.getSuit()) == 0) {
							if (card1.getSuit().compareTo(trump) == 0) {
								sumForTwentysAndFortys += 40;
							} else {
								sumForTwentysAndFortys += 20;
							}
						}
					}
				} else if (card1.getValue().compareTo(Value.KING) == 0) {
					final Card card2 = hand.get(j);
					if (card2.getValue().compareTo(Value.OVER_KNAVE) == 0) {
						if (card1.getSuit().compareTo(card2.getSuit()) == 0) {
							if (card1.getSuit().compareTo(trump) == 0) {
								sumForTwentysAndFortys += 40;
							} else {
								sumForTwentysAndFortys += 20;
							}
						}
					}
				}
			}
		}

		return sumForTwentysAndFortys;
	}

	public boolean isThereForty(final Suit trump) {
		boolean isThereOverKnave = false;
		boolean isThereKing = false;
		for (final Card cardInHand : hand) {
			if (cardInHand.getSuit().compareTo(trump) == 0) {
				if (cardInHand.getValue().compareTo(Value.OVER_KNAVE) == 0) {
					isThereOverKnave = true;
				} else if (cardInHand.getValue().compareTo(Value.KING) == 0) {
					isThereKing = true;
				}
			}
		}

		if (isThereKing && isThereOverKnave) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isThereTrumpSeven(final Suit trump) {
		for (final Card cardInHand : hand) {
			if (cardInHand.getSuit().compareTo(trump) == 0) {
				if (cardInHand.getValue().compareTo(Value.SEVEN) == 0) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean isThereTwenty(final Suit trump) {
		for (final Suit suit : Suit.values()) {
			if (suit.compareTo(trump) != 0) {
				boolean isThereOverKnave = false;
				boolean isThereKing = false;
				for (final Card cardInHand : hand) {
					if (cardInHand.getSuit().compareTo(suit) == 0) {
						if (cardInHand.getValue().compareTo(Value.OVER_KNAVE) == 0) {
							isThereOverKnave = true;
						} else if (cardInHand.getValue().compareTo(Value.KING) == 0) {
							isThereKing = true;
						}
					}
				}

				if (isThereKing && isThereOverKnave) {
					return true;
				}
			}
		}

		return false;
	}

}
