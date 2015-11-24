package managers;

import interfaces.IChatRoomManager;
import interfaces.IMessageHandler;
import interfaces.IPasswordHasher;
import interfaces.IPlayerManager;
import interfaces.IPlayerRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import messagers.MessageHandler;
import messagers.util.LoginAnswer;
import messagers.util.LogoutAnswer;
import messagers.util.RegisterAnswer;
import messagers.util.TopListAnswer;
import util.PasswordHasher;
import dal.PlayerRepository;
import domain.ActivePlayer;
import domain.Player;
import domain.PlayerTypeClass.PlayerType;

public class PlayerManager implements IPlayerManager {

	private final IPlayerRepository playerRepository = new PlayerRepository();
	private final IMessageHandler messageHandler = new MessageHandler();
	private final IPasswordHasher passwordHasher = new PasswordHasher();
	private static final IChatRoomManager chatRoomManager = new ChatRoomManager();

	private static int guestNumber = 0;

	@Override
	public void login(final String name, final String pass,
			final ActivePlayer activePlayer) {
		final Player player = playerRepository.get(name);
		boolean arePasswordsEqual = false;
		try {
			arePasswordsEqual = passwordHasher
					.check(pass, player.getPassword());
		} catch (final Exception e) {
			e.printStackTrace();
		}
		if (arePasswordsEqual) {
			activePlayer.setPlayer(player);
			if (player.getType().compareTo(PlayerType.ADMIN) == 0) {
				loginSuccess(activePlayer);
			} else {
				loginSuccess(activePlayer);
			}
		} else {
			this.messageHandler.send(new LoginAnswer(false, "", ""),
					activePlayer);
		}
	}

	@Override
	public void logout(final ActivePlayer activePlayer) {
		activePlayer.setPlayer(null);
		logoutSuccess(activePlayer);
	}

	@Override
	public void register(final String name, final String email,
			final String pass, final ActivePlayer activePlayer) {

		final Player player = new Player();

		player.setName(name);
		player.setEmail(email);
		try {
			player.setPassword(passwordHasher.getSaltedHash(pass));
		} catch (final Exception e1) {
			e1.printStackTrace();
		}
		player.setType(PlayerType.NORMAL);
		try {
			playerRepository.add(player);
			this.messageHandler
			.send(new RegisterAnswer(true, ""), activePlayer);
		} catch (final SQLException e) {
			if (e.getErrorCode() == 1062) {
				if (e.getMessage().contains("name")) {
					messageHandler.send(new RegisterAnswer(false,
							"Ezzel a névvel már regisztráltak!"), activePlayer);
				} else if (e.getMessage().contains("email")) {
					messageHandler.send(new RegisterAnswer(false,
							"Ezzel az emaillel már regisztráltak!"),
							activePlayer);
				}
			} else {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void guestLogin(final ActivePlayer activePlayer) {
		final Player player = new Player();
		final String name = "Vendég#" + guestNumber;
		final String pass = "";

		player.setName(name);
		player.setPassword(pass);
		player.setType(PlayerType.GUEST);
		activePlayer.setPlayer(player);

		guestNumber++;

		loginSuccess(activePlayer);
	}

	@Override
	public void getTopList(final ActivePlayer activePlayer) {
		final List<HashMap<String, Integer>> topList = playerRepository
				.listOrderedByPoint();
		messageHandler.send(new TopListAnswer(topList), activePlayer);
	}

	private void loginSuccess(final ActivePlayer activePlayer) {
		chatRoomManager.addPlayerToRoom(activePlayer,
				ChatRoomManager.globalChatName);
		activePlayer.setLoggedIn(true);

		this.messageHandler.send(new LoginAnswer(true, activePlayer.getPlayer()
				.getName(), activePlayer.getPlayer().getType().toString()
				.toLowerCase()), activePlayer);
	}

	private void logoutSuccess(final ActivePlayer activePlayer) {
		chatRoomManager.deletePlayerFromRoom(activePlayer);
		activePlayer.setLoggedIn(false);

		this.messageHandler.send(new LogoutAnswer(true), activePlayer);
	}

}
