package managers;

import ulti.UltiGame;
import interfaces.IMessageHandler;
import interfaces.IUltiRoomManager;
import managers.util.UltiRoom;
import messagers.MessageHandler;
import domain.ActivePlayer;

public class UltiRoomManager extends RoomManager implements IUltiRoomManager{

	private IMessageHandler messageHandler = new MessageHandler();
	private UltiGame ultiGame;
	
	@Override
	public boolean newRoom(String roomName, int maxSize,
			ActivePlayer activePlayer) {
		UltiRoom room = new UltiRoom(roomName, maxSize);

		if(super.addRoom(room)) {
//			messageHandler.send(new NewChatAnswer(true), activePlayer);
			return true;
		}
		
//		messageHandler.send(new NewChatAnswer(false), activePlayer);
		return false;
	}

	@Override
	public void addPlayerToRoom(ActivePlayer activePlayer, String toRoomName) {
		UltiRoom room = super.getRoom(toRoomName);
		
		if(room != null) {
			if(room.add(activePlayer)) {
//				messageHandler.send(new ToChatAnswer(toRoomName, true), activePlayer);
				if(room.isFull()) {
					ultiGame = new UltiGame(room.getAllPlayers());
					//TODO üzenet a playereknek
				}
			} else {
//				messageHandler.send(new ToChatAnswer("Tele van a szoba!", false), activePlayer);
			}
		} else {
//			messageHandler.send(new ToChatAnswer("Ilyen nevû szoba nem létezik!", false), activePlayer);
		}
		
	}

	@Override
	public void deletePlayerFromRoom(ActivePlayer activePlayer) {
		UltiRoom room = activePlayer.getUltiRoom();
		
		if(room != null) {
			room.remove(activePlayer);
		}
		
	}

}
