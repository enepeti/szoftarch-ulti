package interfaces;

import model.ActivePlayer;

public interface IPlayerManager {

	public void login(String name, String pass, ActivePlayer activePlayer);
	
	public void guestLogin(ActivePlayer activePlayer);
	
	public void register(String name, String email, String pass, final ActivePlayer activePlayer);
	
}
