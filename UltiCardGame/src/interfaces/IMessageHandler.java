package interfaces;

import javax.websocket.Session;

import messagers.util.AnswerMessage;

public interface IMessageHandler {

	public void handle(String message, Session session);
	
	public <T extends AnswerMessage> void send(T messageObject, Session session);
	
}
