package messagers;

import interfaces.IChatRoomManager;
import interfaces.IMessageHandler;
import interfaces.IMessageSender;
import interfaces.IPlayerManager;
import interfaces.ISessionManager;

import java.util.List;

import managers.ChatRoomManager;
import managers.PlayerManager;
import managers.SessionManager;
import messagers.util.ActivePlayerListAnswer;
import messagers.util.AllChatAnswer;
import messagers.util.AnswerMessage;
import messagers.util.ErrorAnswer;
import messagers.util.MessageType.Type;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import domain.ActivePlayer;
import domain.PlayerTypeClass.PlayerType;

public class MessageHandler implements IMessageHandler {

	private static IPlayerManager playerManager = new PlayerManager();
	private static ISessionManager sessionManager = new SessionManager();
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
				} else if (type.toUpperCase().equals(Type.LOGOUT.toString())) {
					if (activePlayer.getPlayer().getType()
							.compareTo(PlayerType.GUEST) != 0) {
						playerManager.logout(activePlayer);
					} else {
						send(new ErrorAnswer(
								"Vendégként nem tudsz kijelentkezni."),
								activePlayer);
					}
				} else if (type.toUpperCase()
						.equals(Type.GUESTLOGIN.toString())) {
					playerManager.guestLogin(activePlayer);
				} else if (type.toUpperCase().equals(Type.CHAT.toString())) {
					this.chatMessage(jsonObject, activePlayer);
				} else if (type.toUpperCase().equals(Type.NEWCHAT.toString())) {
					if (activePlayer.getPlayer().getType()
							.compareTo(PlayerType.GUEST) == 0) {
						send(new ErrorAnswer(
								"Vendégként nem tudsz létrehozni szobát, ha akarsz, jelentkezz be!"),
								activePlayer);
					} else {
						this.newChatMessage(jsonObject, activePlayer);
					}
				} else if (type.toUpperCase().equals(Type.TOCHAT.toString())) {
					this.toChatMessage(jsonObject, activePlayer);
				} else if (type.toUpperCase().equals(Type.LEAVECHAT.toString())) {
					chatRoomManager.deletePlayerFromRoom(activePlayer);
				} else if (type.toUpperCase()
						.equals(Type.GETALLCHAT.toString())) {
					allChatMessage(activePlayer);
				} else if (type.toUpperCase().equals(
						Type.LISTACTIVEPLAYERS.toString())) {
					if (activePlayer.getPlayer().getType()
							.compareTo(PlayerType.ADMIN) == 0) {
						activePlayerListMessage(activePlayer);
					} else {
						send(new ErrorAnswer(
								"Nem adminként nem lehet listázni a játékosokat."),
								activePlayer);
					}
				} else if (type.toUpperCase().equals(Type.KICK.toString())) {
					if (activePlayer.getPlayer().getType()
							.compareTo(PlayerType.ADMIN) == 0) {
						kickMessage(jsonObject, activePlayer);
					} else {
						send(new ErrorAnswer(
								"Nem adminként nem lehet kidobni játékost."),
								activePlayer);
					}
				} else if (type.toUpperCase()
						.equals(Type.GETTOPLIST.toString())) {
					playerManager.getTopList(activePlayer);
				}
			}
		}
	}

	@Override
	public <T extends AnswerMessage> void send(final T messageObject,
			final ActivePlayer activePlayer) {
		if(activePlayer != null) {
			final Gson gson = new Gson();
			final String json = gson.toJson(messageObject);
			this.messageSender.sendMessage(json, activePlayer);
		}
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
			if (chatRoomManager.newRoom(roomName, maxSize, activePlayer)) {
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

	private void allChatMessage(final ActivePlayer activePlayer) {
		final List<String> allRoomNames = chatRoomManager.getAllRoomNames();
		send(new AllChatAnswer(allRoomNames), activePlayer);
	}

	private void activePlayerListMessage(final ActivePlayer activePlayer) {
		final List<String> nameList = sessionManager.getAllActivePlayerNames();
		send(new ActivePlayerListAnswer(nameList), activePlayer);
	}

	private void kickMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String name = "";

		if ((jsonObject.get("name") != null)
				&& !jsonObject.get("name").isJsonNull()) {
			name = jsonObject.get("name").getAsString();
			sessionManager.kickPlayer(name, activePlayer);
		}
	}

}