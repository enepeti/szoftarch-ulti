package messagers.util;

public class LoginAnswer extends AnswerMessage {
	
	private boolean success;
	
	public LoginAnswer(boolean success) {
		super("login");
		this.success = success;
	}


	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	} 
}
