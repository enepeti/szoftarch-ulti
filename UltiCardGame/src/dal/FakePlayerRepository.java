package dal;

import interfaces.dal.IPlayerRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.Player;

public class FakePlayerRepository implements IPlayerRepository {

	private static List<Player> players = new ArrayList<Player>();

	@Override
	public void add(final Player player) {
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
	public void updatePoint(final Player player) {
		final Player playerToUpdate = this.get(player.getName());
		playerToUpdate.setName(player.getName());
		playerToUpdate.setEmail(player.getEmail());
		playerToUpdate.setPassword(player.getPassword());
	}

	@Override
	public List<Player> list() {
		return players;
	}

	@Override
	public int getPoint(final String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<HashMap<String, Integer>> listOrderedByPoint() {
		// TODO Auto-generated method stub
		return null;
	}

}