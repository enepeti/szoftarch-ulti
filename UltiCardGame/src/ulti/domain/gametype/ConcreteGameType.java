package ulti.domain.gametype;

import java.util.ArrayList;
import java.util.List;

public class ConcreteGameType {

	private final List<GameType> gameTypeList = new ArrayList<GameType>();

	private final String name;
	private final int value;
	private final int partyValue;
	private final boolean isTenUp;
	private final boolean isThereTrump;
	private final boolean isItRed;

	public ConcreteGameType(final String name, final int value,
			final int partyValue, final boolean isTenUp,
			final boolean isThereTrump, final boolean isItRed) {
		super();
		this.name = name;
		this.value = value;
		this.partyValue = partyValue;
		this.isTenUp = isTenUp;
		this.isThereTrump = isThereTrump;
		this.isItRed = isItRed;
	}

	public List<GameType> getGameTypeList() {
		return gameTypeList;
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

	public boolean isItRed() {
		return isItRed;
	}

}
