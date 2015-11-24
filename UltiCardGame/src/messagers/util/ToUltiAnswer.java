package messagers.util;

public class ToUltiAnswer extends AnswerMessage {

	private final String message;
	private final boolean success;

	public ToUltiAnswer(final String name, final boolean success) {
		super("toulti");
		this.message = name;
		this.success = success;
	}

	public String getName() {
		return message;
	}

	public boolean isSucces() {
		return success;
	}

}
