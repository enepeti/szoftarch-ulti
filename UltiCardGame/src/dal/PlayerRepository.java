package dal;

import interfaces.IPlayerRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import domain.Player;
import domain.PlayerType;

public class PlayerRepository implements IPlayerRepository {

	private static ConnectionBuilder connectionBuilder;
	private PreparedStatement preparedStatement;
	private Statement statement;
	private ResultSet resultSet;

	public PlayerRepository() {
		connectionBuilder = new ConnectionBuilder();
	}

	@Override
	public void add(final Player player) throws SQLException {
		try {
			preparedStatement = connectionBuilder
					.getConnection()
					.prepareStatement(
							"INSERT INTO player (name, email, password, isadmin) VALUES (?, ?, ?, 0)");
			preparedStatement.setString(1, player.getName());
			preparedStatement.setString(2, player.getEmail());
			preparedStatement.setString(3, player.getPassword());

			preparedStatement.execute();
		} catch (final SQLException e) {
			throw e;
		}
	}

	@Override
	public void remove(final Player player) {
		try {
			preparedStatement = connectionBuilder.getConnection()
					.prepareStatement("DELETE FROM player WHERE name = ?");
			preparedStatement.setString(1, player.getName());
			preparedStatement.execute();
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
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
					player.setName(resultSet.getString("name"));
					player.setEmail(resultSet.getString("email"));
					player.setPassword(resultSet.getString("password"));
					final int isAdmin = resultSet.getInt("isadmin");
					if (isAdmin == 1) {
						player.setType(PlayerType.ADMIN);
					} else if (isAdmin == 0) {
						player.setType(PlayerType.NORMAL);
					}

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
		try {
			preparedStatement = connectionBuilder
					.getConnection()
					.prepareStatement(
							"UPDATE player SET email = ?, password = ? where name = ?");
			preparedStatement.setString(1, player.getEmail());
			preparedStatement.setString(2, player.getPassword());
			preparedStatement.setString(3, player.getName());
			preparedStatement.execute();
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public List<Player> list() {
		final ArrayList<Player> playerList = new ArrayList<Player>();
		try {
			statement = connectionBuilder.getConnection().createStatement();
			resultSet = statement.executeQuery("SELECT * FROM player");

			while (resultSet.next()) {
				final String nameInDb = resultSet.getString("name");
				if (nameInDb != null) {
					final Player player = new Player();
					player.setName(resultSet.getString("name"));
					player.setEmail(resultSet.getString("email"));
					player.setPassword(resultSet.getString("password"));
					final int isAdmin = resultSet.getInt("isadmin");
					if (isAdmin == 1) {
						player.setType(PlayerType.ADMIN);
					} else if (isAdmin == 0) {
						player.setType(PlayerType.NORMAL);
					}

					playerList.add(player);
				}
			}

			return playerList;
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

}
