package dal;

import interfaces.IPlayerRepository;

import java.util.ArrayList;
import java.util.List;

import domain.Player;

public class FakePlayerRepository implements IPlayerRepository {

	private static List<Player> players = new ArrayList<Player>();
	private static int lastId = 0;

	@Override
	public void add(final Player player) {
		lastId++;
		player.setId(lastId);
		players.add(player);
	}

	@Override
	public void remove(final Player player) {
		players.remove(player);

	}

	@Override
	public Player get(final String name) {
		for (final Player player : players) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}

	@Override
	public void update(final Player player) {
		final Player playerToUpdate = this.get(player.getName());
		playerToUpdate.setName(player.getName());
		playerToUpdate.setEmail(player.getEmail());
		playerToUpdate.setPassword(player.getPassword());
	}

	@Override
	public List<Player> list() {
		return players;
	}

}