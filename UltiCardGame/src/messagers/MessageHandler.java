package messagers;

import interfaces.IChatManager;
import interfaces.IMessageHandler;
import interfaces.IMessageSender;
import interfaces.IPlayerManager;
import managers.ChatManager;
import managers.PlayerManager;
import messagers.util.AnswerMessage;
import messagers.util.MessageType.Type;
import model.ActivePlayer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MessageHandler implements IMessageHandler {

	private static IPlayerManager playerManager = new PlayerManager();
	private static IChatManager chatManager = new ChatManager();
	private IMessageSender messageSender = new MessageSender();

	@Override
	public void handle(final String message, final ActivePlayer activePlayer) {
		String type = "";

		final JsonElement jelement = new JsonParser().parse(message);
		if (jelement != null) {
			final JsonObject jsonObject = jelement.getAsJsonObject();
			if ((jsonObject.get("type") != null) 
					&& !jsonObject.get("type").isJsonNull()) {
				
				type = jsonObject.get("type").getAsString();
				if (type.toUpperCase().equals(Type.REGISTER.toString())) {
					this.registerMessage(jsonObject, activePlayer);
				} else if (type.toUpperCase().equals(Type.LOGIN.toString())){
					this.loginMessage(jsonObject, activePlayer);
				} else if (type.toUpperCase().equals(Type.GUESTLOGIN.toString())){
					playerManager.guestLogin(activePlayer);
				} else if (type.toUpperCase().equals(Type.CHAT.toString())){
					this.chatMessage(jsonObject, activePlayer);
				}
			}
		}
	}

	@Override
	public <T extends AnswerMessage> void send(final T messageObject, final ActivePlayer activePlayer) {
		Gson gson = new Gson();
		String json = gson.toJson(messageObject);
		this.messageSender.sendMessage(json, activePlayer);
		
	}

	private void registerMessage(JsonObject jsonObject, ActivePlayer activePlayer) {
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
			playerManager.register(name, email, password, activePlayer);
		}
	}
	
	private void loginMessage(JsonObject jsonObject, ActivePlayer activePlayer) {
		String name = "";
		String password = "";
		
		if ((jsonObject.get("name") != null)
				&& !jsonObject.get("name").isJsonNull()
				&& (jsonObject.get("password") != null)
				&& !jsonObject.get("password").isJsonNull()) {
			
			name = jsonObject.get("name").getAsString();
			password = jsonObject.get("password").getAsString();
			playerManager.login(name, password, activePlayer);
		}
	}
	
	private void chatMessage(JsonObject jsonObject, ActivePlayer activePlayer) {
		String message = "";
		
		if ((jsonObject.get("message") != null)
				&& !jsonObject.get("message").isJsonNull()){
			
			message = jsonObject.get("message").getAsString();
			chatManager.Send(message, activePlayer);
		}
	}
	
}