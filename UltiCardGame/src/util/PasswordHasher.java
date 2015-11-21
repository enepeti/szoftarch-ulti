package util;

import interfaces.IPasswordHasher;

import java.security.SecureRandom;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base64;

public class PasswordHasher implements IPasswordHasher {
	// The higher the number of iterations the more
	// expensive computing the hash is for us and
	// also for an attacker.
	private static final int iterations = 20 * 1000;
	private static final int saltLen = 32;
	private static final int desiredKeyLen = 256;

	/**
	 * Computes a salted PBKDF2 hash of given plaintext password suitable for
	 * storing in a database. Empty passwords are not supported.
	 */
	@Override
	public String getSaltedHash(final String password) throws Exception {
		final byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(
				saltLen);
		// store the salt with the password
		return Base64.encodeBase64String(salt) + "$" + hash(password, salt);
	}

	/**
	 * Checks whether given plaintext password corresponds to a stored salted
	 * hash of the password.
	 */
	@Override
	public boolean check(final String password, final String stored)
			throws Exception {
		final String[] saltAndPass = stored.split("\\$");
		if (saltAndPass.length != 2) {
			throw new IllegalStateException(
					"The stored password have the form 'salt$hash'");
		}
		final String hashOfInput = hash(password,
				Base64.decodeBase64(saltAndPass[0]));
		return hashOfInput.equals(saltAndPass[1]);
	}

	// using PBKDF2 from Sun, an alternative is https://github.com/wg/scrypt
	// cf. http://www.unlimitednovelty.com/2012/03/dont-use-bcrypt.html
	@Override
	public String hash(final String password, final byte[] salt)
			throws Exception {
		if ((password == null) || (password.length() == 0)) {
			throw new IllegalArgumentException(
					"Empty passwords are not supported.");
		}
		final SecretKeyFactory f = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");
		final SecretKey key = f.generateSecret(new PBEKeySpec(password
				.toCharArray(), salt, iterations, desiredKeyLen));
		return Base64.encodeBase64String(key.getEncoded());
	}
}
