package interfaces;

import domain.ActivePlayer;

public interface IChatManager {

	public void Send(String message, ActivePlayer activePlayer);
	
}
