package messagers.util;

public class ToChatAnswer extends AnswerMessage {

	private final boolean success;

	public ToChatAnswer(final boolean success) {
		super("tochat");
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

}
