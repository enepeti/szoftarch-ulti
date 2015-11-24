package messagers.util;

import java.util.List;

public class AllUltiAnswer extends AnswerMessage {

	private List<String> rooms;

	public AllUltiAnswer(final List<String> allRoomNames) {
		super("allulti");
		this.setAllRoomNames(allRoomNames);
	}

	public List<String> getAllRoomNames() {
		return rooms;
	}

	public void setAllRoomNames(final List<String> allRoomNames) {
		this.rooms = allRoomNames;
	}
}
