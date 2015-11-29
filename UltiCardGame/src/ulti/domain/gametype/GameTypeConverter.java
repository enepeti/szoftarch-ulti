package ulti.domain.gametype;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GameTypeConverter {

	private static final Map<Integer, ConcreteGameType> gameTypeMap = new HashMap<Integer, ConcreteGameType>();

	public GameTypeConverter() {
		final GameTypeParty party = new GameTypeParty();
		final GameTypeFortyHundred fortyHundred = new GameTypeFortyHundred();
		final GameTypeUlti ulti = new GameTypeUlti();
		final GameTypeBetli betli = new GameTypeBetli();
		final GameTypeDurchmarsch durchmars = new GameTypeDurchmarsch();
		final GameTypeTwentyHundred twentyHundred = new GameTypeTwentyHundred();

		final ConcreteGameType concreteGameTypeParty = new ConcreteGameType(
				"Parti", 0, 1, true, true, false, true);
		concreteGameTypeParty.getGameTypeList().add(party);
		gameTypeMap.put(1, concreteGameTypeParty);

		final ConcreteGameType concreteGameTypeRedParty = new ConcreteGameType(
				"Piros Parti", 0, 2, true, true, true, true);
		concreteGameTypeRedParty.getGameTypeList().add(party);
		gameTypeMap.put(2, concreteGameTypeRedParty);

		final ConcreteGameType concreteGameTypeFortyHundred = new ConcreteGameType(
				"40-100", 4, 0, true, true, false, false);
		concreteGameTypeFortyHundred.getGameTypeList().add(fortyHundred);
		gameTypeMap.put(3, concreteGameTypeFortyHundred);

		final ConcreteGameType concreteGameTypeUlti = new ConcreteGameType(
				"Ulti", 4, 1, true, true, false, true);
		concreteGameTypeUlti.getGameTypeList().add(ulti);
		gameTypeMap.put(5, concreteGameTypeUlti);

		final ConcreteGameType concreteGameTypeBetli = new ConcreteGameType(
				"Betli", 5, 0, false, false, false, false);
		concreteGameTypeBetli.getGameTypeList().add(betli);
		gameTypeMap.put(6, concreteGameTypeBetli);

		final ConcreteGameType concreteGameTypeDurchmars = new ConcreteGameType(
				"Durchmars", 6, 0, true, true, false, false);
		concreteGameTypeDurchmars.getGameTypeList().add(durchmars);
		gameTypeMap.put(7, concreteGameTypeDurchmars);

		final ConcreteGameType concreteGameTypeUltiFortyHundred = new ConcreteGameType(
				"Ulti 40-100", 8, 0, true, true, false, false);
		concreteGameTypeUltiFortyHundred.getGameTypeList().add(ulti);
		concreteGameTypeUltiFortyHundred.getGameTypeList().add(fortyHundred);
		gameTypeMap.put(10, concreteGameTypeUltiFortyHundred);

		final ConcreteGameType concreteGameTypeUltiRedFortyHundred = new ConcreteGameType(
				"Piros 40-100", 8, 0, true, true, true, false);
		concreteGameTypeUltiRedFortyHundred.getGameTypeList().add(fortyHundred);
		gameTypeMap.put(11, concreteGameTypeUltiRedFortyHundred);

		final ConcreteGameType concreteGameTypeUltiTwentyHundred = new ConcreteGameType(
				"20-100", 8, 0, true, true, false, false);
		concreteGameTypeUltiTwentyHundred.getGameTypeList().add(twentyHundred);
		gameTypeMap.put(12, concreteGameTypeUltiTwentyHundred);

		final ConcreteGameType concreteGameTypeRedUlti = new ConcreteGameType(
				"Piros Ulti", 8, 2, true, true, true, true);
		concreteGameTypeRedUlti.getGameTypeList().add(ulti);
		gameTypeMap.put(15, concreteGameTypeRedUlti);

		final ConcreteGameType concreteGameTypeReBetli = new ConcreteGameType(
				"Rebetli", 10, 0, false, false, false, false);
		concreteGameTypeReBetli.getGameTypeList().add(betli);
		gameTypeMap.put(16, concreteGameTypeReBetli);

		final ConcreteGameType concreteGameTypeFortyHundredDurchmars = new ConcreteGameType(
				"40-100 Durchmars", 10, 0, true, true, false, false);
		concreteGameTypeFortyHundredDurchmars.getGameTypeList().add(
				fortyHundred);
		concreteGameTypeFortyHundredDurchmars.getGameTypeList().add(durchmars);
		gameTypeMap.put(17, concreteGameTypeFortyHundredDurchmars);

		final ConcreteGameType concreteGameTypeUltiDurchmars = new ConcreteGameType(
				"Ulti Durchmars", 10, 0, true, true, false, false);
		concreteGameTypeUltiDurchmars.getGameTypeList().add(ulti);
		concreteGameTypeUltiDurchmars.getGameTypeList().add(durchmars);
		gameTypeMap.put(18, concreteGameTypeUltiDurchmars);
	}

	public static Map<Integer, ConcreteGameType> getGametypemap() {
		return gameTypeMap;
	}

	public ConcreteGameType convertIntToConcreteGameType(final int key) {
		return gameTypeMap.get(key);
	}

	public int convertConcreteGameTypeToInt(
			final ConcreteGameType concreteGameType) {
		if (concreteGameType != null) {
			for (final Entry<Integer, ConcreteGameType> entry : gameTypeMap
					.entrySet()) {
				if (concreteGameType.getName().equals(
						entry.getValue().getName())) {
					return entry.getKey();
				}
			}
		}

		return 0;
	}
}
