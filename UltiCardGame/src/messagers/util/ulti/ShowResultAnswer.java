package messagers.util.ulti;

import java.util.HashMap;

import messagers.util.AnswerMessage;

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
