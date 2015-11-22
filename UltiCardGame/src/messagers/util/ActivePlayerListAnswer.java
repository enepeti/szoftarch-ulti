package messagers.util;

import java.util.List;

public class ActivePlayerListAnswer extends AnswerMessage {

	private List<String> nameList;

	public ActivePlayerListAnswer(final List<String> nameList) {
		super("activeplayerlist");
		this.nameList = nameList;
	}

	public List<String> getNameList() {
		return nameList;
	}

	public void setNameList(final List<String> nameList) {
		this.nameList = nameList;
	}

}
