package messagers.util.ulti;

import java.util.List;

import messagers.util.AnswerMessage;
import ulti.domain.Card;

public class TakeCardsAnswer extends AnswerMessage {

	private final String name;
	private final List<Card> cards;

	public TakeCardsAnswer(final String name, final List<Card> cards) {
		super("takecards");
		this.name = name;
		this.cards = cards;
	}

	public String getName() {
		return name;
	}

	public List<Card> getCards() {
		return cards;
	}

}
