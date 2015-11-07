package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBuilder {
	private Connection connection;

	public ConnectionBuilder() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (final ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final String url = "jdbc:mysql://localhost:3306/ulticardgame";

		try {
			connection = DriverManager.getConnection(url, "ulticardgame",
					"SeriousUltiPassword");
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return connection;
	}
}