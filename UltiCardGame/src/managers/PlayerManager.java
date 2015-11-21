package managers;

import interfaces.IChatRoomManager;
import interfaces.IMessageHandler;
import interfaces.IPasswordHasher;
import interfaces.IPlayerManager;
import interfaces.IPlayerRepository;

import java.sql.SQLException;

import messagers.MessageHandler;
import messagers.util.LoginAnswer;
import messagers.util.RegisterAnswer;
import util.PasswordHasher;
import dal.PlayerRepository;
import domain.ActivePlayer;
import domain.Player;
import domain.PlayerType;

public class PlayerManager implements IPlayerManager {

	private final IPlayerRepository playerRepository = new PlayerRepository();
	private final IMessageHandler messageHandler = new MessageHandler();
	private final IPasswordHasher passwordHasher = new PasswordHasher();
	private final IChatRoomManager chatRoomManager = new ChatRoomManager();

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
			loginSuccess(activePlayer);
		} else {
			this.messageHandler.send(new LoginAnswer(false), activePlayer);
		}
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

	private void loginSuccess(final ActivePlayer activePlayer) {
		chatRoomManager.addPlayerToRoom(activePlayer,
				ChatRoomManager.globalChatName);
		activePlayer.setLoggedIn(true);

		this.messageHandler.send(new LoginAnswer(true), activePlayer);
	}

}
