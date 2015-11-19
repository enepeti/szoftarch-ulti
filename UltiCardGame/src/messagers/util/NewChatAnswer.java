package messagers.util;

public class NewChatAnswer extends AnswerMessage {

	private final boolean success;

	public NewChatAnswer(final boolean success) {
		super("newchat");
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

}
