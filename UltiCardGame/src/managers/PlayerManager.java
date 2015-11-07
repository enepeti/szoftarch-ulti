package managers;

import interfaces.IMessageHandler;
import interfaces.IPasswordHasher;
import interfaces.IPlayerManager;
import interfaces.IPlayerRepository;

import java.util.List;

import javax.websocket.Session;

import model.Player;

public class PlayerManager implements IPlayerManager {

	private IPlayerRepository playerRepository;
	private IMessageHandler messageHandler;
	private IPasswordHasher passwordHasher;
	
	private static List<Player> activePlayers;
	
	public PlayerManager(IPlayerRepository playerRepository, IMessageHandler messageHandler, IPasswordHasher passwordHasher) {
		this.playerRepository = playerRepository;
		this.messageHandler = messageHandler;
		this.passwordHasher = passwordHasher;
	}
	
	@Override
	public boolean login(String name, String pass, Session session) {
		Player player = playerRepository.get(name);
		if(player != null) {
			if(passwordHasher.areEqual(pass, player.getPassword())) {
				player.setCurrentSession(session);
				activePlayers.add(player);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean register(String name, String email, String pass) {
		if(playerRepository.get(name) == null) {
			Player player = new Player();
			player.setName(name);
			player.setEmail(email);
			player.setPassword(passwordHasher.hash(pass));
			playerRepository.add(player);	
			return true;
		}
		return false;
	}

}
