package interfaces;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/ulti")
public interface IMessageReceiver {
	
	@OnOpen
	public void open(Session session);
	
	@OnClose
	public void close(Session session);
	
	@OnError
	public void error(Throwable error);
	
	@OnMessage
	public void handleMessage(String message, Session session);
	
}
