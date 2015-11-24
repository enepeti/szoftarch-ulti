package messagers.util.guest;

import java.util.HashMap;
import java.util.List;

import messagers.util.AnswerMessage;

public class TopListAnswer extends AnswerMessage {

	private final List<HashMap<String, Integer>> topList;

	public TopListAnswer(final List<HashMap<String, Integer>> topList) {
		super("toplist");
		this.topList = topList;
	}

	public List<HashMap<String, Integer>> getTopList() {
		return topList;
	}

}
