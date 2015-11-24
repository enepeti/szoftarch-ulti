package messagers.util.chat.room;

import messagers.util.AnswerMessage;

public class ToChatAnswer extends AnswerMessage {

	private final String message;
	private final boolean success;

	public ToChatAnswer(final String name, final boolean success) {
		super("tochat");
		this.message = name;
		this.success = success;
	}

	public String getName() {
		return message;
	}

	public boolean isSucces() {
		return success;
	}

}
