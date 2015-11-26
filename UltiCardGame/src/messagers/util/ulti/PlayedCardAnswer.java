package messagers.util.ulti;

import messagers.util.AnswerMessage;
import ulti.domain.Card;

public class PlayedCardAnswer extends AnswerMessage {

	private final String name;
	private final boolean isItMe;
	private final Card card;

	public PlayedCardAnswer(final String name, final boolean isItMe,
			final Card card) {
		super("playedcard");
		this.name = name;
		this.card = card;
		this.isItMe = isItMe;
	}

	public String getName() {
		return name;
	}

	public Card getCard() {
		return card;
	}

	public boolean isItMe() {
		return isItMe;
	}

}
