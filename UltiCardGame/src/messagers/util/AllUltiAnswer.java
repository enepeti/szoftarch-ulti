package messagers.util;

import java.util.List;

public class AllUltiAnswer extends AnswerMessage {

	private List<PlayersInUltiRoom> playersInUltiRoom;

	public AllUltiAnswer(final List<PlayersInUltiRoom> playersInUltiRoom) {
		super("allulti");
		this.playersInUltiRoom = playersInUltiRoom;
	}

	public List<PlayersInUltiRoom> getPlayersInUltiRoom() {
		return playersInUltiRoom;
	}

	public void setPlayersInUltiRoom(
			final List<PlayersInUltiRoom> playersInUltiRoom) {
		this.playersInUltiRoom = playersInUltiRoom;
	}
}
