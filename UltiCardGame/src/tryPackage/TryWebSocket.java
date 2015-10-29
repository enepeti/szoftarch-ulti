package tryPackage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/try")
public class TryWebSocket {

	private static Set<Session> connections = new HashSet<Session>();

	@OnOpen
	public void open(Session session) {
		connections.add(session);
		System.out.println("new session: " + session);
		System.out.println("all sessions are:");
		for (Session s : connections) {
			System.out.println(s);
		}
	}

	@OnClose
	public void close(Session session) {
		connections.remove(session);
		System.out.println("bye session");
	}

	@OnError
	public void onError(Throwable error) {
		System.err.println(error.getMessage());
	}

	@OnMessage
	public void handleMessage(String message, Session senderSession) {
		System.out.println("new msg: " + message + "from: " + senderSession);
		for (Session session : connections) {
			try {
				System.out.println("Send msg to session: " + session);
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
