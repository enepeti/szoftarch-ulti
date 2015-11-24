package ulti.domain;

import ulti.domain.SuitType.Suit;
import ulti.domain.ValueType.Value;

public class Card {
	private Suit suit;
	private Value value;

	public Card(final Suit suit, final Value value) {
		super();
		this.suit = suit;
		this.value = value;
	}

	public Suit getSuit() {
		return suit;
	}

	public void setSuit(final Suit suit) {
		this.suit = suit;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(final Value value) {
		this.value = value;
	}

	@Override
	public String toString() {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(suit.toString());
		stringBuilder.append(" ");
		stringBuilder.append(value.toString());

		return stringBuilder.toString();
	}
}
