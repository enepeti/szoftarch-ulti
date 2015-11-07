package managers;

import interfaces.IMessageHandler;
import interfaces.IPasswordHasher;
import interfaces.IPlayerManager;
import interfaces.IPlayerRepository;

import java.util.List;

import javax.websocket.Session;

import model.Player;

public class PlayerManager implements IPlayerManager {

	private final IPlayerRepository playerRepository;
	private final IMessageHandler messageHandler;
	private final IPasswordHasher passwordHasher;

	private static List<Player> activePlayers;

	public PlayerManager(final IPlayerRepository playerRepository,
			final IMessageHandler messageHandler,
			final IPasswordHasher passwordHasher) {
		this.playerRepository = playerRepository;
		this.messageHandler = messageHandler;
		this.passwordHasher = passwordHasher;
	}

	@Override
	public boolean login(final String name, final String pass,
			final Session session) {
		final Player player = playerRepository.get(name);
		if (player != null) {
			if (passwordHasher.areEqual(pass, player.getPassword())) {
				player.setCurrentSession(session);
				activePlayers.add(player);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean register(final String name, final String email,
			final String pass) {
		if (playerRepository.get(name) == null) {
			final Player player = new Player();
			player.setName(name);
			player.setEmail(email);
			player.setPassword(passwordHasher.hash(pass));
			playerRepository.add(player);
			return true;
		}

		return false;
	}

}
