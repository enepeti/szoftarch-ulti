package interfaces;

public interface IPasswordHasher {
	
	public String hash(String plain);
	
	public boolean areEqual(String plain, String hash);
}
