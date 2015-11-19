package ulti;

import java.util.ArrayList;

import ulti.domain.Card;
import ulti.domain.gametype.ConcreteGameType;
import domain.ActivePlayer;

public class UltiGame {

	private final ArrayList<ActivePlayer> activePlayerList = new ArrayList<ActivePlayer>(
			3);
	private ActivePlayer starterActivePlayer;
	private Card remainingCard1;
	private Card remainingCard2;
	private ConcreteGameType concreteGameType;

	public ActivePlayer getStarterActivePlayer() {
		return starterActivePlayer;
	}

	public void setStarterActivePlayer(final ActivePlayer starterActivePlayer) {
		this.starterActivePlayer = starterActivePlayer;
	}

	public Card getRemainingCard1() {
		return remainingCard1;
	}

	public void setRemainingCard1(final Card remainingCard1) {
		this.remainingCard1 = remainingCard1;
	}

	public Card getRemainingCard2() {
		return remainingCard2;
	}

	public void setRemainingCard2(final Card remainingCard2) {
		this.remainingCard2 = remainingCard2;
	}

	public ConcreteGameType getConcreteGameType() {
		return concreteGameType;
	}

	public void setConcreteGameType(final ConcreteGameType concreteGameType) {
		this.concreteGameType = concreteGameType;
	}

	public ArrayList<ActivePlayer> getActivePlayerList() {
		return activePlayerList;
	}

	public void deal() {

	}

	public void nextPlayerTurn() {

	}

	public void startGame() {

	}

	public void endGame() {

	}

	public void evaluateTurn() {

	}

}
