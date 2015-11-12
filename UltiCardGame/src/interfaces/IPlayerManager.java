package interfaces;

import model.Player;

public interface IPlayerManager {

	public void login(String name, String pass, Player player);
	
	public void guestLogin(Player player);
	
	public void register(String name, String email, String pass, final Player player);
	
}
