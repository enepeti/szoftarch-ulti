package messagers.util;

public abstract class AnswerMessage {
	
	private String type;
	
	public AnswerMessage(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
