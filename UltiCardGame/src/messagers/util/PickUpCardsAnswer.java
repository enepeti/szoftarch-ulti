package messagers.util;

import ulti.domain.Card;

public class PickUpCardsAnswer extends AnswerMessage {

	private final Card card1;
	private final Card card2;

	public PickUpCardsAnswer(final Card card1, final Card card2) {
		super("pickedupcards");
		this.card1 = card1;
		this.card2 = card2;
	}

	public Card getCard1() {
		return card1;
	}

	public Card getCard2() {
		return card2;
	}

}
