package interfaces.managers;

import domain.ActivePlayer;

public interface IChatManager {

	public void Send(String message, ActivePlayer activePlayer);
	
}
