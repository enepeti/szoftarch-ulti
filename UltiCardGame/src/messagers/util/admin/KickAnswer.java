package messagers.util.admin;

import messagers.util.AnswerMessage;

public class KickAnswer extends AnswerMessage {

	private boolean success;

	public KickAnswer(final boolean success) {
		super("kick");
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(final boolean success) {
		this.success = success;
	}

}
