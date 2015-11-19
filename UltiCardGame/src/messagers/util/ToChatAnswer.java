package messagers.util;

public class ToChatAnswer extends AnswerMessage {

	private final String name;

	public ToChatAnswer(final String name) {
		super("tochat");
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
