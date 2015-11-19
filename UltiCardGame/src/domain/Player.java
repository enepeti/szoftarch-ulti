package domain;

public class Player {
	
	private int id;
	private String name;
	private String email;
	private String password;
	private PlayerType type;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public PlayerType getType() {
		return type;
	}

	public void setType(PlayerType type) {
		this.type = type;
	}
	
}
