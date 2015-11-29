package messagers.util.ulti;

import java.util.ArrayList;
import java.util.List;

import messagers.util.AnswerMessage;

public class StartUltiAnswer extends AnswerMessage {

	private List<String> names;

	public StartUltiAnswer(final List<String> list) {
		super("startulti");
		this.names = list;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(final ArrayList<String> names) {
		this.names = names;
	}

}
