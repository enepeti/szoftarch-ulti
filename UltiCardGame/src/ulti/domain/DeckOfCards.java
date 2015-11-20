package ulti.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ulti.domain.SuitType.Suit;
import ulti.domain.ValueType.Value;

public class DeckOfCards {

	private final List<Card> cards;

	public DeckOfCards() {
		cards = new ArrayList<Card>();

		for (final Suit s : Suit.values()) {
			for (final Value v : Value.values()) {
				final Card card = new Card(s, v);
				cards.add(card);
			}
		}
	}

	public List<Card> getCards() {
		return cards;
	}

	public void shuffle() {
		Collections.shuffle(cards);
	}

}
