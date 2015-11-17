package interfaces;

import messagers.util.AnswerMessage;
import model.ActivePlayer;

public interface IMessageHandler {

	public void handle(String message, ActivePlayer activePlayer);
	
	public <T extends AnswerMessage> void send(T messageObject, ActivePlayer activePlayer);
	
}
