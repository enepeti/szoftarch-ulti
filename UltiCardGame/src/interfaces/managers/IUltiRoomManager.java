package interfaces.managers;

import java.util.List;

import domain.ActivePlayer;

public interface IUltiRoomManager extends IRoomManager, IUltiManager {

	public List<String> getAllRoomNames();

	void someoneLeavesRoom(ActivePlayer activePlayer);

}
