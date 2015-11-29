package messagers.util.ulti;

import java.util.HashMap;

import messagers.util.AnswerMessage;

public class StartUltiGameAnswer extends AnswerMessage {

	private final HashMap<String, Integer> points;

	public StartUltiGameAnswer(final HashMap<String, Integer> points) {
		super("startgame");
		this.points = points;
	}

	public HashMap<String, Integer> getPoints() {
		return points;
	}

}
