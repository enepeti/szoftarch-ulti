package interfaces.managers;

import java.util.List;

import messagers.util.chat.room.ChatRoomSizeShower;

public interface IChatRoomManager extends IRoomManager, IChatManager {

	public List<ChatRoomSizeShower> getAllChatRooms();
}
