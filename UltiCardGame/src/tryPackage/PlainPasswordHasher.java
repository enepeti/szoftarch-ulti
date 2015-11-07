package tryPackage;

import interfaces.IPasswordHasher;

public class PlainPasswordHasher implements IPasswordHasher {

	@Override
	public String hash(final String plain) {
		return plain;
	}

	@Override
	public boolean areEqual(final String plain, final String hash) {
		return (plain.equals(hash));
	}

}
