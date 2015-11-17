package interfaces;

import model.ActivePlayer;

public interface IMessageSender {

	public void sendMessage(String message, ActivePlayer activePlayer);
	
}
