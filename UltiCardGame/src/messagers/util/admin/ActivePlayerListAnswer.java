package messagers.util.admin;

import java.util.List;

import messagers.util.AnswerMessage;

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
