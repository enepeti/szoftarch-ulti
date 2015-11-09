package messagers.util;

public class ErrorAnswer extends AnswerMessage {

	private String message;
	
	public ErrorAnswer(String message) {
		super("error");
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
