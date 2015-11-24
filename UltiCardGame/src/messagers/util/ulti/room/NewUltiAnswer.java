package messagers.util.ulti.room;

import messagers.util.AnswerMessage;

public class NewUltiAnswer extends AnswerMessage {

	private final boolean success;

	public NewUltiAnswer(final boolean success) {
		super("newulti");
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

}
