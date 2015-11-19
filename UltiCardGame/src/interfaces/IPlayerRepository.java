package interfaces;

import java.util.List;

import domain.Player;

public interface IPlayerRepository {

	public void add(Player player);

	public void remove(Player player);

	public Player get(int id);

	public Player get(String name);
	
	public boolean isUniqueName(String name);
	
	public boolean isUniqueEmail(String email);

	public void update(Player player);

	public List<Player> list();

}
