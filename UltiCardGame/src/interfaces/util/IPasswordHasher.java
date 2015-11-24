package interfaces.util;

public interface IPasswordHasher {

	public String getSaltedHash(final String password) throws Exception;

	public String hash(final String password, final byte[] salt)
			throws Exception;

	public boolean check(final String password, final String stored)
			throws Exception;
}
