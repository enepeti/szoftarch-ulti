package messagers;

import interfaces.IMessageHandler;
import interfaces.IPlayerManager;

import java.io.IOException;

import javax.websocket.Session;

import managers.PlayerManager;
import messagers.util.AnswerMessage;
import messagers.util.MessageType.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MessageHandler implements IMessageHandler {

	private static IPlayerManager playerManager = new PlayerManager();
			//new PlayerRepository(), null, new PlainPasswordHasher());

	@Override
	public void handle(final String message, final Session session) {
		String type = "";

		final JsonElement jelement = new JsonParser().parse(message);
		if (jelement != null) {
			final JsonObject jsonObject = jelement.getAsJsonObject();
			if ((jsonObject.get("type") != null) 
					&& !jsonObject.get("type").isJsonNull()) {
				
				type = jsonObject.get("type").getAsString();
				if (type.toUpperCase().equals(Type.REGISTER.toString())) {
					registerMessage(jsonObject, session);
				} else if (type.toUpperCase().equals(Type.LOGIN.toString())){
					loginMessage(jsonObject, session);
				} else if (type.toUpperCase().equals(Type.GUESTLOGIN.toString())){
					playerManager.guestLogin(session);
				} else if (type.toUpperCase().equals(Type.CHAT.toString())){
					chatMessage(jsonObject, session);
				}
			}
		}

		
	}

	@Override
	public <T extends AnswerMessage> void send(final T messageObject, final Session session) {
		Gson gson = new Gson();
		String json = gson.toJson(messageObject);
		try {
			session.getBasicRemote().sendText(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void registerMessage(JsonObject jsonObject, Session session) {
		String name = "";
		String email = "";
		String password = "";
		
		if ((jsonObject.get("name") != null)
				&& !jsonObject.get("name").isJsonNull()
				&& (jsonObject.get("email") != null)
				&& !jsonObject.get("email").isJsonNull()
				&& (jsonObject.get("password") != null)
				&& !jsonObject.get("password").isJsonNull()) {
			
			name = jsonObject.get("name").getAsString();
			email = jsonObject.get("email").getAsString();
			password = jsonObject.get("password").getAsString();
			playerManager.register(name, email, password, session);
		}
	}
	
	private void loginMessage(JsonObject jsonObject, Session session) {
		String name = "";
		String password = "";
		
		if ((jsonObject.get("name") != null)
				&& !jsonObject.get("name").isJsonNull()
				&& (jsonObject.get("email") != null)
				&& !jsonObject.get("email").isJsonNull()
				&& (jsonObject.get("password") != null)
				&& !jsonObject.get("password").isJsonNull()) {
			
			name = jsonObject.get("name").getAsString();
			password = jsonObject.get("password").getAsString();
			playerManager.login(name, password, session);
		}
	}
	
	private void chatMessage(JsonObject jsonObject, Session session) {
		String message = "";
		
		if ((jsonObject.get("name") != null)
				&& !jsonObject.get("name").isJsonNull()
				&& (jsonObject.get("email") != null)
				&& !jsonObject.get("email").isJsonNull()
				&& (jsonObject.get("password") != null)
				&& !jsonObject.get("password").isJsonNull()) {
			
			message = jsonObject.get("message").getAsString();
			
		}
	}
	
}
