package messageHandlers;

import interfaces.IMessageHandler;
import interfaces.IPlayerManager;

import javax.websocket.Session;

import managers.PlayerManager;
import messageHandlers.util.MessageType.Type;
import tryPackage.PlainPasswordHasher;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dal.FakePlayerRepository;

public class MessageHandler implements IMessageHandler {

	private static IPlayerManager playerManager = new PlayerManager(
			new FakePlayerRepository(), null, new PlainPasswordHasher());

	@Override
	public void handle(final String message, final Session session) {
		String type = "";
		String name = "";
		String email = "";
		String password = "";

		final JsonElement jelement = new JsonParser().parse(message);
		if (jelement != null) {
			final JsonObject jsonObject = jelement.getAsJsonObject();
			if ((jsonObject.get("type") != null)
					&& !jsonObject.get("type").isJsonNull()
					&& (jsonObject.get("name") != null)
					&& !jsonObject.get("name").isJsonNull()
					&& (jsonObject.get("email") != null)
					&& !jsonObject.get("email").isJsonNull()
					&& (jsonObject.get("password") != null)
					&& !jsonObject.get("password").isJsonNull()) {
				type = jsonObject.get("type").getAsString();
				name = jsonObject.get("name").getAsString();
				email = jsonObject.get("email").getAsString();
				password = jsonObject.get("password").getAsString();
			}
		}

		if (type.toUpperCase().equals(Type.REGISTER.toString())) {
			playerManager.register(name, email, password);
		}
	}

	@Override
	public <T> void send(final T messageObject, final Session session) {
		// TODO Auto-generated method stub
	}

}
