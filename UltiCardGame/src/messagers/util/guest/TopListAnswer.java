package messagers.util.guest;

import java.util.HashMap;

import messagers.util.AnswerMessage;

public class TopListAnswer extends AnswerMessage {

	private final HashMap<String, Integer> topList;

	public TopListAnswer(final HashMap<String, Integer> topList) {
		super("toplist");
		this.topList = topList;
	}

	public HashMap<String, Integer> getTopList() {
		return topList;
	}

}
