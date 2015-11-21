package messagers.util;

public class LogoutAnswer extends AnswerMessage {

	private boolean success;

	public LogoutAnswer(final boolean success) {
		super("logout");
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(final boolean success) {
		this.success = success;
	}
}
