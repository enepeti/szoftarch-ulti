package messagers.util;

import java.util.HashMap;

public class ShowResultAnswer extends AnswerMessage {

	private final HashMap<String, Integer> points;

	public ShowResultAnswer(final HashMap<String, Integer> points) {
		super("showresult");
		this.points = points;
	}

	public HashMap<String, Integer> getPoints() {
		return points;
	}

}
