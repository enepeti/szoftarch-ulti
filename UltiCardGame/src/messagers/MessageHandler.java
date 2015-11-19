package messagers;

import interfaces.IChatRoomManager;
import interfaces.IMessageHandler;
import interfaces.IMessageSender;
import interfaces.IPlayerManager;
import managers.ChatRoomManager;
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
	private static IChatRoomManager chatRoomManager = new ChatRoomManager();
	private final IMessageSender messageSender = new MessageSender();

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
				} else if (type.toUpperCase().equals(Type.LOGIN.toString())) {
					this.loginMessage(jsonObject, activePlayer);
				} else if (type.toUpperCase()
						.equals(Type.GUESTLOGIN.toString())) {
					playerManager.guestLogin(activePlayer);
				} else if (type.toUpperCase().equals(Type.CHAT.toString())) {
					this.chatMessage(jsonObject, activePlayer);
				} else if (type.toUpperCase().equals(Type.NEWCHAT.toString())) {
					this.newChatMessage(jsonObject, activePlayer);
				} else if (type.toUpperCase().equals(Type.TOCHAT.toString())) {
					this.toChatMessage(jsonObject, activePlayer);
				} else if (type.toUpperCase().equals(Type.LEAVECHAT.toString())) {
					chatRoomManager.deletePlayerFromRoom(activePlayer);
				}
			}
		}
	}

	@Override
	public <T extends AnswerMessage> void send(final T messageObject,
			final ActivePlayer activePlayer) {
		final Gson gson = new Gson();
		final String json = gson.toJson(messageObject);
		this.messageSender.sendMessage(json, activePlayer);

	}

	private void registerMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
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

	private void loginMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
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

	private void chatMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String message = "";

		if ((jsonObject.get("message") != null)
				&& !jsonObject.get("message").isJsonNull()) {

			message = jsonObject.get("message").getAsString();
			chatRoomManager.Send(message, activePlayer);
		}
	}

	private void newChatMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String roomName = "";
		final int maxSize;

		if (((jsonObject.get("name") != null) && !jsonObject.get("name")
				.isJsonNull())) {
			roomName = jsonObject.get("name").getAsString();
			maxSize = jsonObject.get("maxmembers").getAsInt();
			if(chatRoomManager.newRoom(roomName, maxSize)) {
				chatRoomManager.changePlayerRoom(activePlayer, roomName);
			}
		}
	}

	private void toChatMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String toRoomName = "";

		if (((jsonObject.get("name") != null) && !jsonObject.get("name")
				.isJsonNull())) {
			toRoomName = jsonObject.get("name").getAsString();
			chatRoomManager.changePlayerRoom(activePlayer, toRoomName);
		}
	}
}