package messagers.util;

import java.util.List;

public class PlayersInUltiRoom {

	private String roomName;
	private List<String> names;

	public PlayersInUltiRoom(final String roomName, final List<String> names) {
		this.roomName = roomName;
		this.names = names;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(final String roomName) {
		this.roomName = roomName;
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(final List<String> names) {
		this.names = names;
	}

}
