package messagers;

import interfaces.IChatManager;
import interfaces.IMessageHandler;
import interfaces.IMessageSender;
import interfaces.IPlayerManager;
import managers.ChatManager;
import managers.PlayerManager;
import messagers.util.AnswerMessage;
import messagers.util.MessageType.Type;
import model.Player;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MessageHandler implements IMessageHandler {

	private static IPlayerManager playerManager = new PlayerManager();
	private static IChatManager chatManager = new ChatManager();
	private IMessageSender messageSender = new MessageSender();
			//new PlayerRepository(), null, new PlainPasswordHasher());

	@Override
	public void handle(final String message, final Player player) {
		String type = "";

		final JsonElement jelement = new JsonParser().parse(message);
		if (jelement != null) {
			final JsonObject jsonObject = jelement.getAsJsonObject();
			if ((jsonObject.get("type") != null) 
					&& !jsonObject.get("type").isJsonNull()) {
				
				type = jsonObject.get("type").getAsString();
				if (type.toUpperCase().equals(Type.REGISTER.toString())) {
					this.registerMessage(jsonObject, player);
				} else if (type.toUpperCase().equals(Type.LOGIN.toString())){
					this.loginMessage(jsonObject, player);
				} else if (type.toUpperCase().equals(Type.GUESTLOGIN.toString())){
					this.playerManager.guestLogin(player);
				} else if (type.toUpperCase().equals(Type.CHAT.toString())){
					this.chatMessage(jsonObject, player);
				}
			}
		}
	}

	@Override
	public <T extends AnswerMessage> void send(final T messageObject, final Player player) {
		Gson gson = new Gson();
		String json = gson.toJson(messageObject);
		this.messageSender.sendMessage(json, player);
		
	}

	private void registerMessage(JsonObject jsonObject, Player player) {
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
			playerManager.register(name, email, password, player);
		}
	}
	
	private void loginMessage(JsonObject jsonObject, Player player) {
		String name = "";
		String password = "";
		
		if ((jsonObject.get("name") != null)
				&& !jsonObject.get("name").isJsonNull()
				&& (jsonObject.get("password") != null)
				&& !jsonObject.get("password").isJsonNull()) {
			
			name = jsonObject.get("name").getAsString();
			password = jsonObject.get("password").getAsString();
			playerManager.login(name, password, player);
		}
	}
	
	private void chatMessage(JsonObject jsonObject, Player player) {
		String message = "";
		
		if ((jsonObject.get("message") != null)
				&& !jsonObject.get("message").isJsonNull()){
			
			message = jsonObject.get("message").getAsString();
			chatManager.Send(message, player);
		}
	}
	
}
