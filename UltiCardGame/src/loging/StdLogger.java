package loging;

public class StdLogger implements Logger {

	@Override
	public void log(String message) {
		System.out.println(message);
	}

}
