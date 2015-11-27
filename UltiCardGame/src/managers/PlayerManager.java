package managers;

import interfaces.dal.IPlayerRepository;
import interfaces.managers.IChatRoomManager;
import interfaces.managers.IPlayerManager;
import interfaces.managers.ISessionManager;
import interfaces.managers.IUltiRoomManager;
import interfaces.messagers.IMessageHandler;
import interfaces.util.IPasswordHasher;

import java.sql.SQLException;
import java.util.HashMap;

import messagers.MessageHandler;
import messagers.util.error.ErrorAnswer;
import messagers.util.guest.TopListAnswer;
import messagers.util.userhandling.LoginAnswer;
import messagers.util.userhandling.LogoutAnswer;
import messagers.util.userhandling.RegisterAnswer;
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
	private static final IUltiRoomManager ultiRoomManager = new UltiRoomManager();
	private static final ISessionManager sessionManager = new SessionManager();

	private static int guestNumber = 0;

	@Override
	public void login(final String name, final String pass,
			final ActivePlayer activePlayer) {
		if (checkIfNoUserAlreadyIn(name, activePlayer)) {
			final Player player = playerRepository.get(name);
			if (player != null) {
				boolean arePasswordsEqual = true;
				try {
					arePasswordsEqual = passwordHasher.check(pass,
							player.getPassword());
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
			} else {
				this.messageHandler.send(new LoginAnswer(false, "", ""),
						activePlayer);
			}
		} else {
			this.messageHandler
			.send(new ErrorAnswer(
					"Ezzel a felhasználónévvel már be vannak lépve a játékba!"),
					activePlayer);
		}
	}

	private boolean checkIfNoUserAlreadyIn(final String name,
			final ActivePlayer activePlayer) {
		if (sessionManager.getActivePlayerForPlayerName(name) == null) {
			return true;
		}

		return false;
	}

	@Override
	public void logout(final ActivePlayer activePlayer) {
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
		final HashMap<String, Integer> topList = playerRepository
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
		activePlayer.setPlayer(null);
		activePlayer.setUltiPlayer(null);
		chatRoomManager.deletePlayerFromRoom(activePlayer);
		ultiRoomManager.deletePlayerFromRoom(activePlayer);
		activePlayer.setLoggedIn(false);

		this.messageHandler.send(new LogoutAnswer(), activePlayer);
	}
}
