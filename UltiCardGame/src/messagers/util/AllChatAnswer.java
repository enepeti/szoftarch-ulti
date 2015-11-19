package messagers.util;

public class AllChatAnswer extends AnswerMessage {

	private String[] allRoomNames;

	public AllChatAnswer(final String[] allRoomNames) {
		super("allchat");
		this.setAllRoomNames(allRoomNames);
	}

	public String[] getAllRoomNames() {
		return allRoomNames;
	}

	public void setAllRoomNames(final String[] allRoomNames) {
		this.allRoomNames = allRoomNames;
	}
}
