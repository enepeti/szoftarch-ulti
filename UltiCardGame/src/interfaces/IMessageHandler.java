package interfaces;

import messagers.util.AnswerMessage;
import model.Player;

public interface IMessageHandler {

	public void handle(String message, Player player);
	
	public <T extends AnswerMessage> void send(T messageObject, Player player);
	
}
