package ulti.domain;

import java.util.ArrayList;
import java.util.List;

import ulti.domain.ValueType.Value;
import ulti.domain.gametype.ConcreteGameType;

public class UltiPlayer {

	private final List<Card> hand = new ArrayList<Card>();
	private int pointsInGamesInActualRoom = 0;
	private final List<Card> taken = new ArrayList<Card>();

	public List<Card> getHand() {
		return hand;
	}

	public int getPointsInGamesInActualRoom() {
		return pointsInGamesInActualRoom;
	}

	public void setPointsInGamesInActualRoom(final int pointsInGamesInActualRoom) {
		this.pointsInGamesInActualRoom = pointsInGamesInActualRoom;
	}

	public void addCardsToHand(final List<Card> cardsToHand) {
		hand.addAll(cardsToHand);
	}

	public void addCardsToTaken(final List<Card> cardsToTaken) {
		taken.addAll(cardsToTaken);
	}

	public void say(final ConcreteGameType concreteGameType, final Card card1,
			final Card card2) {
		hand.remove(card1);
		hand.remove(card2);
	}

	public void pass() {

	}

	public void pickUpCards(final Card card1, final Card card2) {
		hand.add(card1);
		hand.add(card2);
	}

	public void playCard(final Card card) {
		hand.remove(card);
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
}
