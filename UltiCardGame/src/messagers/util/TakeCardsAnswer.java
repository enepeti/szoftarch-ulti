package messagers.util;

import java.util.List;

import ulti.domain.Card;

public class TakeCardsAnswer extends AnswerMessage {

	private final List<Card> cards;

	public TakeCardsAnswer(final List<Card> cards) {
		super("takecards");
		this.cards = cards;
	}

	public List<Card> getCards() {
		return cards;
	}
}
