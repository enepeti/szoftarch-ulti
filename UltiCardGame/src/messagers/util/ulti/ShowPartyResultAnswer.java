package messagers.util.ulti;

import java.util.HashMap;

import messagers.util.AnswerMessage;

public class ShowPartyResultAnswer extends AnswerMessage {

	private final HashMap<String, Integer> points;

	public ShowPartyResultAnswer(final HashMap<String, Integer> points) {
		super("showpartyresult");
		this.points = points;
	}

	public HashMap<String, Integer> getPoints() {
		return points;
	}

}
