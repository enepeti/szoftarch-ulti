package messagers.util.chat.room;

import java.util.List;

import messagers.util.AnswerMessage;

public class AllChatAnswer extends AnswerMessage {

	private List<String> rooms;

	public AllChatAnswer(final List<String> allRoomNames) {
		super("allchat");
		this.setAllRoomNames(allRoomNames);
	}

	public List<String> getAllRoomNames() {
		return rooms;
	}

	public void setAllRoomNames(List<String> allRoomNames) {
		this.rooms = allRoomNames;
	}
}
