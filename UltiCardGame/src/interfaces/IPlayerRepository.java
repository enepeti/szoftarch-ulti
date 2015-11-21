package interfaces;

import java.sql.SQLException;
import java.util.List;

import domain.Player;

public interface IPlayerRepository {

	public void add(Player player) throws SQLException;

	public void remove(Player player);

	public Player get(String name);

	public void update(Player player);

	public List<Player> list();

}
