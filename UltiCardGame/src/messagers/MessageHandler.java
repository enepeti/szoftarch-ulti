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
import managers.util.Room;
import messagers.util.AnswerMessage;
import messagers.util.admin.ActivePlayerListAnswer;
import messagers.util.chat.room.AllChatAnswer;
import messagers.util.error.ErrorAnswer;
import messagers.util.messagetype.MessageType.Type;
import messagers.util.ulti.PlayersInUltiRoom;
import messagers.util.ulti.room.AllUltiAnswer;
import ulti.domain.Card;
import ulti.domain.CardConverter;
import ulti.domain.SuitType.Suit;
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
	private static GameTypeConverter gameTypeConverter = new GameTypeConverter();

	@Override
	public void handle(final String message, final ActivePlayer activePlayer) {
		String type = "";

		final JsonElement jelement = new JsonParser().parse(message);
		if (jelement != null) {
			final JsonObject jsonObject = jelement.getAsJsonObject();
			final JsonElement jsonElement = jsonObject.get("type");
			if ((jsonElement != null) && !jsonElement.isJsonNull()) {

				type = jsonElement.getAsString();
				final String upperCaseType = type.toUpperCase();
				if (upperCaseType.equals(Type.REGISTER.toString())) {
					registerMessage(jsonObject, activePlayer);
				} else if (upperCaseType.equals(Type.LOGIN.toString())) {
					loginMessage(jsonObject, activePlayer);
				} else if (upperCaseType.equals(Type.LOGOUT.toString())) {
					playerManager.logout(activePlayer);
				} else if (upperCaseType.equals(Type.GUESTLOGIN.toString())) {
					playerManager.guestLogin(activePlayer);
				} else if (upperCaseType.equals(Type.CHAT.toString())) {
					chatMessage(jsonObject, activePlayer);
				} else if (upperCaseType.equals(Type.NEWCHAT.toString())) {
					if (activePlayer.getPlayer().getType()
							.compareTo(PlayerType.GUEST) == 0) {
						send(new ErrorAnswer(
								"Vendégként nem tudsz létrehozni szobát, ha akarsz, jelentkezz be!"),
								activePlayer);
					} else {
						newChatMessage(jsonObject, activePlayer);
					}
				} else if (upperCaseType.equals(Type.TOCHAT.toString())) {
					toChatMessage(jsonObject, activePlayer);
				} else if (upperCaseType.equals(Type.LEAVECHAT.toString())) {
					chatRoomManager.deletePlayerFromRoom(activePlayer);
				} else if (upperCaseType.equals(Type.GETALLCHAT.toString())) {
					allChatMessage(activePlayer);
				} else if (upperCaseType.equals(Type.NEWULTI.toString())) {
					if (activePlayer.getPlayer().getType()
							.compareTo(PlayerType.GUEST) == 0) {
						send(new ErrorAnswer(
								"Vendégként nem tudsz létrehozni szobát, ha akarsz, jelentkezz be!"),
								activePlayer);
					} else {
						this.newUltiMessage(jsonObject, activePlayer);
					}
				} else if (upperCaseType.equals(Type.TOULTI.toString())) {
					toUltiMessage(jsonObject, activePlayer);
				} else if (upperCaseType.equals(Type.LEAVEULTI.toString())) {
					ultiRoomManager.deletePlayerFromRoom(activePlayer);
				} else if (upperCaseType.equals(Type.GETALLULTI.toString())) {
					allUltiMessage(activePlayer);
				} else if (upperCaseType.equals(Type.LISTACTIVEPLAYERS
						.toString())) {
					if (activePlayer.getPlayer().getType()
							.compareTo(PlayerType.ADMIN) == 0) {
						activePlayerListMessage(activePlayer);
					} else {
						send(new ErrorAnswer(
								"Nem adminként nem lehet listázni a játékosokat."),
								activePlayer);
					}
				} else if (upperCaseType.equals(Type.KICK.toString())) {
					if (activePlayer.getPlayer().getType()
							.compareTo(PlayerType.ADMIN) == 0) {
						kickMessage(jsonObject, activePlayer);
					} else {
						send(new ErrorAnswer(
								"Nem adminként nem lehet kidobni játékost."),
								activePlayer);
					}
				} else if (upperCaseType.equals(Type.GETTOPLIST.toString())) {
					playerManager.getTopList(activePlayer);
				} else if (upperCaseType.equals(Type.GAMESELECTION.toString())) {
					gameSelectionMessage(jsonObject, activePlayer);
				} else if (upperCaseType.equals(Type.PASS.toString())) {
					activePlayer.getUltiRoom().getUltiGame().pass();
				} else if (upperCaseType.equals(Type.PICKUPCARDS.toString())) {
					activePlayer.getUltiRoom().getUltiGame().pickUpCards();
				} else if (upperCaseType.equals(Type.CONFIRMGAME.toString())) {
					confirmMessage(jsonObject, activePlayer);
				} else if (upperCaseType.equals(Type.PLAYCARD.toString())) {
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

		final JsonElement jsonElementName = jsonObject.get("name");
		final JsonElement jsonElementEmail = jsonObject.get("email");
		final JsonElement jsonElementPassword = jsonObject.get("password");
		if ((jsonElementName != null) && !jsonElementName.isJsonNull()
				&& (jsonElementEmail != null) && !jsonElementEmail.isJsonNull()
				&& (jsonElementPassword != null)
				&& !jsonElementPassword.isJsonNull()) {

			name = jsonElementName.getAsString();
			email = jsonElementEmail.getAsString();
			password = jsonElementPassword.getAsString();
			playerManager.register(name, email, password, activePlayer);
		}
	}

	private void loginMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String name = "";
		String password = "";

		final JsonElement jsonElementName = jsonObject.get("name");
		final JsonElement jsonElementPassword = jsonObject.get("password");
		if ((jsonElementName != null) && !jsonElementName.isJsonNull()
				&& (jsonElementPassword != null)
				&& !jsonElementPassword.isJsonNull()) {

			name = jsonElementName.getAsString();
			password = jsonElementPassword.getAsString();
			playerManager.login(name, password, activePlayer);
		}
	}

	private void chatMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String message = "";

		final JsonElement jsonElementMessage = jsonObject.get("message");
		if ((jsonElementMessage != null) && !jsonElementMessage.isJsonNull()) {

			message = jsonElementMessage.getAsString();
			chatRoomManager.Send(message, activePlayer);
		}
	}

	private void newChatMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String roomName = "";
		final int maxSize;

		final JsonElement jsonElementName = jsonObject.get("name");
		if (((jsonElementName != null) && !jsonElementName.isJsonNull())) {
			roomName = jsonElementName.getAsString();
			maxSize = jsonObject.get("maxmembers").getAsInt();
			if (chatRoomManager.newRoom(roomName, maxSize, activePlayer)) {
				chatRoomManager.changePlayerRoom(activePlayer, roomName);
			}
		}
	}

	private void toChatMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String toRoomName = "";

		final JsonElement jsonElementName = jsonObject.get("name");
		if (((jsonElementName != null) && !jsonElementName.isJsonNull())) {
			toRoomName = jsonElementName.getAsString();
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

		final JsonElement jsonElementName = jsonObject.get("name");
		if (((jsonElementName != null) && !jsonElementName.isJsonNull())) {
			roomName = jsonElementName.getAsString();
			maxSize = jsonObject.get("maxmembers").getAsInt();
			if (ultiRoomManager.newRoom(roomName, maxSize, activePlayer)) {
				ultiRoomManager.changePlayerRoom(activePlayer, roomName);
			}
		}
	}

	private void toUltiMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String toRoomName = "";

		final JsonElement jsonElementName = jsonObject.get("name");
		if (((jsonElementName != null) && !jsonElementName.isJsonNull())) {
			toRoomName = jsonElementName.getAsString();
			ultiRoomManager.changePlayerRoom(activePlayer, toRoomName);
		}
	}

	private void allUltiMessage(final ActivePlayer activePlayer) {
		final List<String> allRoomNames = ultiRoomManager.getAllRoomNames();
		final List<PlayersInUltiRoom> playersInUltiRoom = new ArrayList<PlayersInUltiRoom>();
		for (final String roomName : allRoomNames) {
			final Room room = ultiRoomManager.getRoom(roomName);
			final List<String> activePlayerNames = room
					.getActivePlayerNamesInRoom();
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

		final JsonElement jsonElementName = jsonObject.get("name");
		if ((jsonElementName != null) && !jsonElementName.isJsonNull()) {
			name = jsonElementName.getAsString();
			sessionManager.kickPlayer(name, activePlayer);
		}
	}

	private void gameSelectionMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		int gameType = 0;
		JsonObject card1JsonObject;
		JsonObject card2JsonObject;

		final JsonElement jsonElementGameType = jsonObject.get("gameType");
		final JsonElement jsonElementCard1 = jsonObject.get("card1");
		final JsonElement jsonElementCard2 = jsonObject.get("card2");
		if ((jsonElementGameType != null) && !jsonElementGameType.isJsonNull()
				&& (jsonElementCard1 != null) && !jsonElementCard1.isJsonNull()
				&& (jsonElementCard2 != null) && !jsonElementCard2.isJsonNull()) {

			gameType = jsonElementGameType.getAsInt();
			card1JsonObject = jsonElementCard1.getAsJsonObject();
			card2JsonObject = jsonElementCard2.getAsJsonObject();

			final JsonElement jsonElementSuit1 = card1JsonObject.get("suit");
			final JsonElement jsonElementValue1 = card1JsonObject.get("value");
			final JsonElement jsonElementSuit2 = card2JsonObject.get("suit");
			final JsonElement jsonElementValue2 = card2JsonObject.get("value");
			if ((jsonElementSuit1 != null) && !jsonElementSuit1.isJsonNull()
					&& (jsonElementValue1 != null)
					&& !jsonElementValue1.isJsonNull()
					&& (jsonElementSuit2 != null)
					&& !jsonElementSuit2.isJsonNull()
					&& (jsonElementValue2 != null)
					&& !jsonElementValue2.isJsonNull()) {

				final String card1Suit = jsonElementSuit1.getAsString();
				final String card1Value = jsonElementValue1.getAsString();
				final String card2Suit = jsonElementSuit2.getAsString();
				final String card2Value = jsonElementValue2.getAsString();

				final ConcreteGameType concreteGameType = gameTypeConverter
						.convertIntToConcreteGameType(gameType);
				final Card card1 = CardConverter.convertStringsToCard(
						card1Suit, card1Value);
				final Card card2 = CardConverter.convertStringsToCard(
						card2Suit, card2Value);

				activePlayer.getUltiRoom().getUltiGame()
						.say(concreteGameType, card1, card2);
			}
		}
	}

	private void confirmMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		String trumpSuitString = "";

		final JsonElement jsonElementName = jsonObject.get("suit");
		if ((jsonElementName != null) && !jsonElementName.isJsonNull()) {
			trumpSuitString = jsonElementName.getAsString();
			final Suit trumpSuit = CardConverter
					.convertStringToSuit(trumpSuitString);
			activePlayer.getUltiRoom().getUltiGame()
					.confirm(trumpSuit, activePlayer);
		}
	}

	private void playCardMessage(final JsonObject jsonObject,
			final ActivePlayer activePlayer) {
		JsonObject cardJsonObject;

		final JsonElement jsonElementCard = jsonObject.get("card");
		if ((jsonElementCard != null) && !jsonElementCard.isJsonNull()) {
			cardJsonObject = jsonElementCard.getAsJsonObject();

			final JsonElement jsonElementSuit = cardJsonObject.get("suit");
			final JsonElement jsonElementValue = cardJsonObject.get("value");

			if ((jsonElementSuit != null) && !jsonElementSuit.isJsonNull()
					&& (jsonElementValue != null)
					&& !jsonElementValue.isJsonNull()) {

				final String cardSuit = jsonElementSuit.getAsString();
				final String cardValue = jsonElementValue.getAsString();

				final Card card = CardConverter.convertStringsToCard(cardSuit,
						cardValue);

				activePlayer.getUltiRoom().getUltiGame()
						.playCard(card, activePlayer);
			}
		}
	}
}