package ulti.domain;

import ulti.domain.SuitType.Suit;
import ulti.domain.ValueType.Value;

public class CardConverter {

	public static Card convertStringsToCard(final String cardSuit,
			final String cardValue) {
		Suit suit = null;
		Value value = null;

		final String cardSuitUpper = cardSuit.toUpperCase();
		final String cardValueUpper = cardValue.toUpperCase();

		if (Suit.ACORN.toString().equals(cardSuitUpper)) {
			suit = Suit.ACORN;
		} else if (Suit.BELL.toString().equals(cardSuitUpper)) {
			suit = Suit.BELL;
		} else if (Suit.HEART.toString().equals(cardSuitUpper)) {
			suit = Suit.HEART;
		} else if (Suit.LEAF.toString().equals(cardSuitUpper)) {
			suit = Suit.LEAF;
		}

		if (Value.ACE.toString().equals(cardValueUpper)) {
			value = Value.ACE;
		} else if (Value.EIGHT.toString().equals(cardValueUpper)) {
			value = Value.EIGHT;
		} else if (Value.KING.toString().equals(cardValueUpper)) {
			value = Value.KING;
		} else if (Value.NINE.toString().equals(cardValueUpper)) {
			value = Value.NINE;
		} else if (Value.OVER_KNAVE.toString().equals(cardValueUpper)) {
			value = Value.OVER_KNAVE;
		} else if (Value.SEVEN.toString().equals(cardValueUpper)) {
			value = Value.SEVEN;
		} else if (Value.TEN.toString().equals(cardValueUpper)) {
			value = Value.TEN;
		} else if (Value.UNDER_KNAVE.toString().equals(cardValueUpper)) {
			value = Value.UNDER_KNAVE;
		}

		return new Card(suit, value);
	}

}
