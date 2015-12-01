package messagers.util.ulti;

import java.util.List;

import messagers.util.AnswerMessage;
import ulti.domain.Card;

public class DealAnswer extends AnswerMessage {

	private List<Card> cards;
	private boolean isStarter;
	private final String starterName;

	public DealAnswer(final List<Card> cards, final boolean isStarter,
			final String starterName) {
		super("deal");
		this.cards = cards;
		this.isStarter = isStarter;
		this.starterName = starterName;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(final List<Card> cards) {
		this.cards = cards;
	}

	public boolean isStarter() {
		return isStarter;
	}

	public void setStarter(final boolean isStarter) {
		this.isStarter = isStarter;
	}

	public String getStarterName() {
		return starterName;
	}

}
