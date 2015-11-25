package ulti.domain;

import java.util.ArrayList;
import java.util.List;

import ulti.domain.ValueType.Value;
import ulti.domain.gametype.ConcreteGameType;

public class UltiPlayer {

	private List<Card> hand = new ArrayList<Card>(12);
	private int pointsInGamesInActualRoom = 0;
	private List<Card> taken = new ArrayList<Card>(30);

	public List<Card> getHand() {
		return hand;
	}

	public void setHand(final List<Card> hand) {
		this.hand = hand;
	}

	public int getPointsInGamesInActualRoom() {
		return pointsInGamesInActualRoom;
	}

	public void setPointsInGamesInActualRoom(final int pointsInGamesInActualRoom) {
		this.pointsInGamesInActualRoom = pointsInGamesInActualRoom;
	}

	public void addCardsToHand(final List<Card> cardsToHand) {
		hand = new ArrayList<Card>();
		hand.addAll(cardsToHand);
	}

	public void addCardsToTaken(final List<Card> cardsToTaken) {
		taken = new ArrayList<Card>();
		taken.addAll(cardsToTaken);
	}

	public void say(final ConcreteGameType concreteGameType, final Card card1,
			final Card card2) {
		getHand().remove(card1);
		getHand().remove(card2);
	}

	public void pass() {

	}

	public void pickUpCards(final Card card1, final Card card2) {
		getHand().add(card1);
		getHand().add(card2);
	}

	public void playCard(final Card card) {
		getHand().remove(card);
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
