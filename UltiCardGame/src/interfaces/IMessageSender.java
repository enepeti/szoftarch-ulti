package interfaces;

import domain.ActivePlayer;

public interface IMessageSender {

	public void sendMessage(String message, ActivePlayer activePlayer);
	
}
