package messagers.util.ulti;

import messagers.util.AnswerMessage;

public class GameSelectedAnswer extends AnswerMessage {

	private String name;
	private boolean isItMe;
	private int gameType;

	public GameSelectedAnswer(final String name, final boolean isItMe,
			final int gameType) {
		super("gameselected");
		this.name = name;
		this.isItMe = isItMe;
		this.gameType = gameType;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isItMe() {
		return isItMe;
	}

	public void setItMe(final boolean isItMe) {
		this.isItMe = isItMe;
	}

	public int getGameType() {
		return gameType;
	}

	public void setGameType(final int gameType) {
		this.gameType = gameType;
	}

}
