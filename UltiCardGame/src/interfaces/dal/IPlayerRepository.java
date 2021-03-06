package interfaces.dal;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import domain.Player;

public interface IPlayerRepository {

	public void add(Player player) throws SQLException;

	public void remove(Player player);

	public Player get(String name);

	public int getPoint(String name);

	public void updatePoint(Player player);

	public List<Player> list();

	public HashMap<String, Integer> listOrderedByPoint();

}
