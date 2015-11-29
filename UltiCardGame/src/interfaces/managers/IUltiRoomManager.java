package interfaces.managers;

import domain.ActivePlayer;

public interface IUltiRoomManager extends IRoomManager, IUltiManager {

	void someoneLeavesRoom(ActivePlayer activePlayer);

}
