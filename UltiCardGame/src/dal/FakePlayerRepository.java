package dal;

import interfaces.IPlayerRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import model.Player;

public class FakePlayerRepository implements IPlayerRepository {

	private static ConnectionBuilder connectionBuilder;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public FakePlayerRepository() {
		connectionBuilder = new ConnectionBuilder();
	}

	@Override
	public void add(final Player player) {
		try {
			preparedStatement = connectionBuilder
					.getConnection()
					.prepareStatement(
							"INSERT INTO player (name, email, password, sessionid) VALUES (?, ?, ?, null)");
			preparedStatement.setString(1, player.getName());
			preparedStatement.setString(2, player.getEmail());
			preparedStatement.setString(3, player.getPassword());

			preparedStatement.execute();
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void remove(final Player player) {
	}

	@Override
	public Player get(final int id) {
		return null;
	}

	@Override
	public Player get(final String name) {
		try {
			preparedStatement = connectionBuilder.getConnection()
					.prepareStatement("SELECT * FROM player WHERE name = ?");
			preparedStatement.setString(1, name);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				final String nameInDb = resultSet.getString("name");
				if (nameInDb != null) {
					final Player player = new Player();
					player.setId(resultSet.getInt("id"));
					player.setName(resultSet.getString("name"));
					player.setEmail(resultSet.getString("email"));
					player.setPassword(resultSet.getString("password"));

					return player;
				}
			}
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void update(final Player player) {
		final Player playerToUpdate = this.get(player.getId());
		playerToUpdate.setName(player.getName());
		playerToUpdate.setEmail(player.getEmail());
		playerToUpdate.setPassword(player.getPassword());
		playerToUpdate.setCurrentSession(player.getCurrentSession());
	}

	@Override
	public List<Player> list() {
		return null;
	}

}
