package messagers.util;

public class LoginAnswer extends AnswerMessage {

	private boolean success;
	private String playerType;

	public LoginAnswer(final boolean success, final String playerType) {
		super("login");
		this.success = success;
		this.setPlayerType(playerType);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(final boolean success) {
		this.success = success;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(final String playerType) {
		this.playerType = playerType;
	}

}
