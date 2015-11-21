package domain;

import domain.PlayerTypeClass.PlayerType;

public class Player {

	private int id;
	private String name;
	private String email;
	private String password;
	private PlayerType type;
	private int point;

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public PlayerType getType() {
		return type;
	}

	public void setType(final PlayerType type) {
		this.type = type;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(final int point) {
		this.point = point;
	}

	public void addPoint(final int point) {
		this.point += point;
	}

}
