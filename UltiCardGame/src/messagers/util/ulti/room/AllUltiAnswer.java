package messagers.util.ulti.room;

import java.util.List;

import messagers.util.AnswerMessage;
import messagers.util.ulti.PlayersInUltiRoom;

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
