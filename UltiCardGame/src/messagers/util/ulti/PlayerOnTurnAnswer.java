package messagers.util.ulti;

import messagers.util.AnswerMessage;

public class PlayerOnTurnAnswer extends AnswerMessage {

	private final String name;

	public PlayerOnTurnAnswer(final String name) {
		super("playeronturn");
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
