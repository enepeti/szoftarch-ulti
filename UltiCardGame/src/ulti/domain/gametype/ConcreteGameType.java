package ulti.domain.gametype;

import java.util.ArrayList;
import java.util.List;

import ulti.domain.SuitType.Suit;

public class ConcreteGameType {

	private final List<GameType> gameTypeList = new ArrayList<GameType>();

	private final String name;
	private final int value;
	private final int partyValue;
	private final boolean isTenUp;
	private final boolean isThereTrump;
	private final boolean isItRed;
	private final boolean isThereParty;
	private Suit trump;

	public ConcreteGameType(final String name, final int value,
			final int partyValue, final boolean isTenUp,
			final boolean isThereTrump, final boolean isItRed,
			final boolean isThereParty) {
		super();
		this.name = name;
		this.value = value;
		this.partyValue = partyValue;
		this.isTenUp = isTenUp;
		this.isThereTrump = isThereTrump;
		this.isItRed = isItRed;
		this.isThereParty = isThereParty;
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

	public boolean isThereParty() {
		return isThereParty;
	}

	public Suit getTrump() {
		return trump;
	}

	public void setTrump(Suit trump) {
		this.trump = trump;
	}

}
