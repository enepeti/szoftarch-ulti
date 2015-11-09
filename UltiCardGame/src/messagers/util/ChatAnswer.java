package messagers.util;

public class ChatAnswer extends AnswerMessage {

	private String message;
	private String from;
	
	public ChatAnswer(String message, String senderName) {
		super("chat");
		this.message = message;
		this.from = senderName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String senderName) {
		this.from = senderName;
	}

}
