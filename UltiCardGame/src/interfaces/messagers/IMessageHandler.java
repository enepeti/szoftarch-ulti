package interfaces.messagers;

import domain.ActivePlayer;
import messagers.util.AnswerMessage;

public interface IMessageHandler {

	public void handle(String message, ActivePlayer activePlayer);
	
	public <T extends AnswerMessage> void send(T messageObject, ActivePlayer activePlayer);
	
}
