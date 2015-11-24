package messagers.util.userhandling;

import messagers.util.AnswerMessage;

public class LoginAnswer extends AnswerMessage {

	private boolean success;
	private String name;
	private String playerType;

	public LoginAnswer(final boolean success, final String name,
			final String playerType) {
		super("login");
		this.success = success;
		this.name = name;
		this.setPlayerType(playerType);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(final boolean success) {
		this.success = success;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getPlayerType() {
		return playerType;
	}

	public void setPlayerType(final String playerType) {
		this.playerType = playerType;
	}

}
