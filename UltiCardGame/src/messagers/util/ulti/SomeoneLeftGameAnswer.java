package messagers.util.ulti;

import messagers.util.AnswerMessage;

public class SomeoneLeftGameAnswer extends AnswerMessage {

	private String name;

	public SomeoneLeftGameAnswer(final String name) {
		super("someoneleftgame");
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
