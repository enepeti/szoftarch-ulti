package messagers;

import interfaces.managers.IChatRoomManager;
import interfaces.managers.IPlayerManager;
import interfaces.managers.ISessionManager;
import interfaces.managers.IUltiRoomManager;
import interfaces.messagers.IMessageHandler;
import interfaces.messagers.IMessageSender;

import java.util.ArrayList;
import java.util.List;

import managers.ChatRoomManager;
import managers.PlayerManager;
import managers.SessionManager;
import managers.UltiRoomManager;
import messagers.util.AnswerMessage;
import messagers.util.admin.ActivePlayerListAnswer;
import messagers.util.chat.room.AllChatAnswer;
import messagers.util.error.ErrorAnswer;
import messagers.util.messagetype.MessageType.Type;
import messagers.util.ulti.PlayersInUltiRoom;
import messagers.util.ulti.room.AllUltiAnswer;
import ulti.domain.Card;
import ulti.domain.CardConverter;
import ulti.domain.gametype.ConcreteGameType;
import ulti.domain.gametype.GameTypeConverter;

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
	private static IUltiRoomManager ultiRoomManager = new UltiRoomManager();
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
				} else if (type.toUpperCase().equals(Type.NEWULTI.toString())) {
					if (activePlayer.getPlayer().getType()
							.compareTo(PlayerType.GUEST) == 0) {
						send(new ErrorAnswer(
								"Vendégként nem tudsz létrehozni szobát, ha akarsz, jelentkezz be!"),
								activePlayer);
					} else {
						this.newUltiMessage(jsonObject, activePlayer);
					}
				} else if (type.toUpperCase().equals(Type.TOULTI.toString())) {
					this.toUltiMessage(jsonObject, activePlayer);
				} else if (type.toUpperCase().equals(Type.LEAVEULTI.toString())) {
					ultiRoomManager.deletePlayerFromRoom(activePlayer);
				} else if (type.toUpperCase()
						.equals(Type.GETALLULTI.toString())) {
					allUltiMessage(activePlayer);
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
				} else if (type.toUpperCase().equals(
						Type.GAMESELECTION.toString())) {
					gameSelectionMessage(jsonObject, activePlayer);
				} else if (type.toUpperCase().equals(Type.PASS.toString())) {
					activePlayer.getUltiRoom().getUltiGame().pass();
				} else if (type.toUpperCase().equals(
						Type.PICKUPCARDS.toString())) {
					activePlayer.getUltiRoom().getUltiGame().pickUpCards();
				} else if (type.toUpperCase().equals(
						Type.CONFIRMGAME.toString())) {
					activePlayer.getUltiRoom().getUltiGame().confirm();
				} else if (type.toUpperCase().equals(Type.PLAYCARD.toString())) {
					playCardMessage(jsonObject, activePlayer);
				}
			}
		}
	}

	@Override
	public <T extends AnswerMessage> void send(final T messageObject,
			final ActivePlayer activePlayer) {
		if (activePlayer != null) {
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

	private void newUltiMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String roomName = "";
		final int maxSize;

		if (((jsonObject.get("name") != null) && !jsonObject.get("name")
				.isJsonNull())) {
			roomName = jsonObject.get("name").getAsString();
			maxSize = jsonObject.get("maxmembers").getAsInt();
			if (ultiRoomManager.newRoom(roomName, maxSize, activePlayer)) {
				ultiRoomManager.changePlayerRoom(activePlayer, roomName);
			}
		}

	}

	private void toUltiMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String toRoomName = "";

		if (((jsonObject.get("name") != null) && !jsonObject.get("name")
				.isJsonNull())) {
			toRoomName = jsonObject.get("name").getAsString();
			ultiRoomManager.changePlayerRoom(activePlayer, toRoomName);
		}

	}

	private void allUltiMessage(final ActivePlayer activePlayer) {
		final List<String> allRoomNames = ultiRoomManager.getAllRoomNames();
		final List<PlayersInUltiRoom> playersInUltiRoom = new ArrayList<PlayersInUltiRoom>();
		for (final String roomName : allRoomNames) {
			final List<String> activePlayerNames = ultiRoomManager.getRoom(
					roomName).getActivePlayerNamesInRoom();
			playersInUltiRoom.add(new PlayersInUltiRoom(roomName,
					activePlayerNames));
		}

		send(new AllUltiAnswer(playersInUltiRoom), activePlayer);
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

	private void gameSelectionMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		int gameType = 0;
		String card1String = "";
		String card2String = "";

		if ((jsonObject.get("gameType") != null)
				&& !jsonObject.get("gameType").isJsonNull()
				&& (jsonObject.get("card1") != null)
				&& !jsonObject.get("card1").isJsonNull()
				&& (jsonObject.get("card2") != null)
				&& !jsonObject.get("card2").isJsonNull()) {

			gameType = jsonObject.get("gameType").getAsInt();
			card1String = jsonObject.get("card1").getAsString();
			card2String = jsonObject.get("card2").getAsString();

			final ConcreteGameType concreteGameType = GameTypeConverter
					.convertIntToConcreteGameType(gameType);
			final Card card1 = CardConverter.convertStringToCard(card1String);
			final Card card2 = CardConverter.convertStringToCard(card2String);

			activePlayer.getUltiRoom().getUltiGame()
			.say(concreteGameType, card1, card2);
		}
	}

	private void playCardMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String cardString = "";

		if ((jsonObject.get("card") != null)
				&& !jsonObject.get("card").isJsonNull()) {

			cardString = jsonObject.get("card").getAsString();
			final Card card = CardConverter.convertStringToCard(cardString);

			activePlayer.getUltiRoom().getUltiGame().playCard(card);
		}
	}
}