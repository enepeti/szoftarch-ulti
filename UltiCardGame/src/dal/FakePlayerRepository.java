package dal;

import java.util.ArrayList;
import java.util.List;

import model.Player;
import interfaces.IPlayerRepository;

public class FakePlayerRepository implements IPlayerRepository {

	private static List<Player> players = new ArrayList<Player>();
	private static int lastId = 0;
	
	@Override
	public void add(Player player) {
		lastId++;
		player.setId(lastId);
		players.add(player);
	}

	@Override
	public void remove(Player player) {
		players.remove(player);

	}

	@Override
	public Player get(int id) {
		return players.get(id);
	}
	
	@Override
	public Player get(String name) {
		for (Player player : players) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}

	@Override
	public void update(Player player) {
		Player playerToUpdate = this.get(player.getId());
		playerToUpdate.setName(player.getName());
		playerToUpdate.setEmail(player.getEmail());
		playerToUpdate.setPassword(player.getPassword());
	}

	@Override
	public List<Player> list() {
		return players;
	}

	@Override
	public boolean isUniqueName(String name) {
		for (Player player : players) {
			if (player.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean isUniqueEmail(String email) {
		for (Player player : players) {
			if (player.getName().equals(email)) {
				return false;
			}
		}
		return true;
	}

}