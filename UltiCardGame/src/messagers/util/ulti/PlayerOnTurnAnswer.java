package messagers.util.ulti;

import messagers.util.AnswerMessage;

public class PlayerOnTurnAnswer extends AnswerMessage {

	private final String name;
	private boolean isItMe;

	public PlayerOnTurnAnswer(final String name, final boolean isItMe) {
		super("playeronturn");
		this.name = name;
		this.isItMe = isItMe;
	}

	public String getName() {
		return name;
	}

	public boolean isItMe() {
		return isItMe;
	}

	public void setItMe(final boolean isItMe) {
		this.isItMe = isItMe;
	}

}
