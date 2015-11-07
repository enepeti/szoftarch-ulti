package tryPackage;

import interfaces.IPasswordHasher;

public class PlainPasswordHasher implements IPasswordHasher {

	@Override
	public String hash(String plain) {
		return plain;
	}

	@Override
	public boolean areEqual(String plain, String hash) {
		return (plain.equals(hash));
	}

	
	
}
