package ulti.domain;

import ulti.domain.SuitType.Suit;
import ulti.domain.ValueType.Value;

public class CardConverter {

	public static Card convertStringToCard(final String cardName) {
		final String[] NameArray = cardName.split(" ");
		final String suitString = NameArray[0];
		final String valueString = NameArray[1];

		Suit suit = null;
		Value value = null;

		final String suitStringUpper = suitString.toUpperCase();
		if (Suit.ACORN.toString().equals(suitStringUpper)) {
			suit = Suit.ACORN;
		} else if (Suit.BELL.toString().equals(suitStringUpper)) {
			suit = Suit.BELL;
		} else if (Suit.HEART.toString().equals(suitStringUpper)) {
			suit = Suit.HEART;
		} else if (Suit.LEAF.toString().equals(suitStringUpper)) {
			suit = Suit.LEAF;
		}

		final String valueStringUpper = valueString.toUpperCase();
		if (Value.ACE.toString().equals(valueStringUpper)) {
			value = Value.ACE;
		} else if (Value.EIGHT.toString().equals(valueStringUpper)) {
			value = Value.EIGHT;
		} else if (Value.KING.toString().equals(valueStringUpper)) {
			value = Value.KING;
		} else if (Value.NINE.toString().equals(valueStringUpper)) {
			value = Value.NINE;
		} else if (Value.OVER_KNAVE.toString().equals(valueStringUpper)) {
			value = Value.OVER_KNAVE;
		} else if (Value.SEVEN.toString().equals(valueStringUpper)) {
			value = Value.SEVEN;
		} else if (Value.TEN.toString().equals(valueStringUpper)) {
			value = Value.TEN;
		} else if (Value.UNDER_KNAVE.toString().equals(valueStringUpper)) {
			value = Value.UNDER_KNAVE;
		}

		return new Card(suit, value);
	}

}
