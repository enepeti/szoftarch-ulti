package messagers.util.chat.room;

import java.util.List;

import messagers.util.AnswerMessage;

public class AllChatAnswer extends AnswerMessage {

	private List<ChatRoomSizeShower> rooms;

	public AllChatAnswer(final List<ChatRoomSizeShower> rooms) {
		super("allchat");
		this.rooms = rooms;
	}

	public List<ChatRoomSizeShower> getRooms() {
		return rooms;
	}

	public void setRooms(final List<ChatRoomSizeShower> rooms) {
		this.rooms = rooms;
	}

}
