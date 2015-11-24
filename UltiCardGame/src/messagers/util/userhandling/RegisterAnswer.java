package messagers.util.userhandling;

import messagers.util.AnswerMessage;

public class RegisterAnswer extends AnswerMessage{

	private boolean success;
	private String fault;
	
	public RegisterAnswer(boolean success, String fault) {
		super("register");
		this.setSuccess(success);
		this.setFault(fault);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getFault() {
		return fault;
	}

	public void setFault(String fault) {
		this.fault = fault;
	}

}
