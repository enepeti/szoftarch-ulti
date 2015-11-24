package dal;

import interfaces.dal.IPlayerRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import domain.Player;
import domain.PlayerTypeClass.PlayerType;

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
							"INSERT INTO player (name, email, password, isadmin, point) VALUES (?, ?, ?, 0, 0)");
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
					player.setPoint(resultSet.getInt("point"));

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
	public int getPoint(final String name) {
		try {
			preparedStatement = connectionBuilder
					.getConnection()
					.prepareStatement("SELECT point FROM player WHERE name = ?");
			preparedStatement.setString(1, name);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				final int point = resultSet.getInt("point");

				return point;
			}
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public void updatePoint(final Player player) {
		try {
			preparedStatement = connectionBuilder.getConnection()
					.prepareStatement(
							"UPDATE player SET point = ? where name = ?");
			preparedStatement.setInt(1, player.getPoint());
			preparedStatement.setString(2, player.getName());
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
					player.setPoint(resultSet.getInt("point"));

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

	@Override
	public ArrayList<HashMap<String, Integer>> listOrderedByPoint() {
		final ArrayList<HashMap<String, Integer>> topList = new ArrayList<HashMap<String, Integer>>();
		try {
			statement = connectionBuilder.getConnection().createStatement();
			resultSet = statement
					.executeQuery("SELECT name, point FROM player ORDER BY point DESC");

			while (resultSet.next()) {
				final String nameInDb = resultSet.getString("name");
				if (nameInDb != null) {
					final int point = resultSet.getInt("point");
					final HashMap<String, Integer> map = new HashMap<String, Integer>();
					map.put(nameInDb, point);
					topList.add(map);
				}
			}

			return topList;
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
}
