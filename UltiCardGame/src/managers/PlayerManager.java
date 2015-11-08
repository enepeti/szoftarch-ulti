package managers;

import interfaces.IMessageHandler;
import interfaces.IPasswordHasher;
import interfaces.IPlayerManager;
import interfaces.IPlayerRepository;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

import messagers.MessageHandler;
import messagers.util.LoginAnswer;
import messagers.util.RegisterAnswer;
import model.Player;
import model.PlayerType;
import tryPackage.PlainPasswordHasher;
import dal.FakePlayerRepository;

public class PlayerManager implements IPlayerManager {

	private final IPlayerRepository playerRepository = new FakePlayerRepository();
	private final IMessageHandler messageHandler = new MessageHandler(); 
	private final IPasswordHasher passwordHasher = new PlainPasswordHasher();

	private static List<Player> activePlayers = new ArrayList<Player>();
	private static int guestNumber = 0;

//	public PlayerManager(final IPlayerRepository playerRepository,
//			final IMessageHandler messageHandler,
//			final IPasswordHasher passwordHasher) {
//		this.playerRepository = playerRepository;
//		this.messageHandler = messageHandler;
//		this.passwordHasher = passwordHasher;
//	}

	@Override
	public void login(final String name, final String pass,
			final Session session) {
		final Player player = playerRepository.get(name);
		if (player != null) {
			if (passwordHasher.areEqual(pass, player.getPassword())) {
				player.setCurrentSession(session);
				activePlayers.add(player);
				this.messageHandler.send(new LoginAnswer(true), session);
				return;
			}
		}
		this.messageHandler.send(new LoginAnswer(false), session);
	}

	@Override
	public void register(final String name, final String email,
			final String pass, Session session) {
		if (playerRepository.isUniqueName(name)) {
			if(playerRepository.isUniqueEmail(email)) {
				final Player player = new Player();
				player.setName(name);
				player.setEmail(email);
				player.setPassword(passwordHasher.hash(pass));
				player.setType(PlayerType.NORMAL);
				playerRepository.add(player);
				this.messageHandler.send(new RegisterAnswer(true, ""), session);
			} else {
				this.messageHandler.send(new RegisterAnswer(false, "Ezzel az emaillel már regisztráltak!"), session);
			}
		} else {
			this.messageHandler.send(new RegisterAnswer(false, "Ezzel a névvel már regisztráltak!"), session);
		}
	}

	@Override
	public void guestLogin(Session session) {
		final Player player = new Player();
		final String name = "Vendég#" + guestNumber;
		final String pass = passwordHasher.hash(name);
		player.setName(name);
		player.setPassword(pass);
		player.setType(PlayerType.GUEST);
		player.setCurrentSession(session);
		guestNumber++;
		//KELLE? playerRepository.add(player);
		activePlayers.add(player);
		this.messageHandler.send(new LoginAnswer(true), session);
	}

}
