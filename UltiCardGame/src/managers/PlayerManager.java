package managers;

import interfaces.IMessageHandler;
import interfaces.IPasswordHasher;
import interfaces.IPlayerManager;
import interfaces.IPlayerRepository;
import messagers.MessageHandler;
import messagers.util.LoginAnswer;
import messagers.util.RegisterAnswer;
import model.ActivePlayer;
import model.Player;
import model.PlayerType;
import tryPackage.PlainPasswordHasher;
import dal.PlayerRepository;

public class PlayerManager implements IPlayerManager {

	private final IPlayerRepository playerRepository = new PlayerRepository();
<<<<<<< Updated upstream
	private final IMessageHandler messageHandler = new MessageHandler(); 
=======
	private final IMessageHandler messageHandler = new MessageHandler();
>>>>>>> Stashed changes
	private final IPasswordHasher passwordHasher = new PlainPasswordHasher();

	private static int guestNumber = 0;

	@Override
	public void login(final String name, final String pass,
			final ActivePlayer activePlayer) {
		final Player player = playerRepository.get(name);
		if (passwordHasher.areEqual(pass, player.getPassword())) {
			activePlayer.setPlayer(player);
			loginSuccess(activePlayer);
		} else {
			this.messageHandler.send(new LoginAnswer(false), activePlayer);
		}
	}

	@Override
	public void register(final String name, final String email,
			final String pass, final ActivePlayer activePlayer) {

		if (playerRepository.isUniqueName(name)) {
			if (playerRepository.isUniqueEmail(email)) {
				final Player player = new Player();

				player.setName(name);
				player.setEmail(email);
				player.setPassword(passwordHasher.hash(pass));
				player.setType(PlayerType.NORMAL);
				playerRepository.add(player);
				this.messageHandler.send(new RegisterAnswer(true, ""),
						activePlayer);
			} else {
				this.messageHandler.send(new RegisterAnswer(false,
						"Ezzel az emaillel már regisztráltak!"), activePlayer);
			}
		} else {
			this.messageHandler.send(new RegisterAnswer(false,
					"Ezzel a névvel már regisztráltak!"), activePlayer);
		}
	}

	@Override
	public void guestLogin(final ActivePlayer activePlayer) {
		final Player player = new Player();
		final String name = "Vendég#" + guestNumber;
		final String pass = passwordHasher.hash(name);

		player.setName(name);
		player.setPassword(pass);
		player.setType(PlayerType.GUEST);
		activePlayer.setPlayer(player);

		guestNumber++;

		loginSuccess(activePlayer);
	}

	private void loginSuccess(final ActivePlayer activePlayer) {
		ChatRoomManager.getGlobalChat().add(activePlayer);
		activePlayer.setLoggedIn(true);

		this.messageHandler.send(new LoginAnswer(true), activePlayer);
	}

}
