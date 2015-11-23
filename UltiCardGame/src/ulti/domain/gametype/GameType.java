package ulti.domain.gametype;

public abstract class GameType {

	private final String name;
	private final int value;
	private final int partyValue;
	private final boolean isTenUp;
	private final boolean isThereTrump;
	private final boolean isThereParty;

	public GameType(final String name, final int value, final int partyValue,
			final boolean isTenUp, final boolean isThereTrump,
			final boolean isThereParty) {
		super();
		this.name = name;
		this.value = value;
		this.partyValue = partyValue;
		this.isTenUp = isTenUp;
		this.isThereTrump = isThereTrump;
		this.isThereParty = isThereParty;
	}

	public int getAllValue() {
		return value + partyValue;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public int getPartyValue() {
		return partyValue;
	}

	public boolean isTenUp() {
		return isTenUp;
	}

	public boolean isThereTrump() {
		return isThereTrump;
	}

	public boolean isThereParty() {
		return isThereParty;
	}
}
