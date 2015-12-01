package messagers.util.ulti;

import java.util.HashMap;

import messagers.util.AnswerMessage;

public class StartUltiGameAnswer extends AnswerMessage {

	private final HashMap<String, Integer> points;
	private String trump;

	public StartUltiGameAnswer(final HashMap<String, Integer> points,
			final String trump) {
		super("startgame");
		this.points = points;
		this.setTrump(trump);
	}

	public HashMap<String, Integer> getPoints() {
		return points;
	}

	public String getTrump() {
		return trump;
	}

	public void setTrump(final String trump) {
		this.trump = trump;
	}

}
