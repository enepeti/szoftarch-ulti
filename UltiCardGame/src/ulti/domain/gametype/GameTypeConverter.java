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

		final ConcreteGameType concreteGameTypeTwentyHundredUlti = new ConcreteGameType(
				"20-100 Ulti", 12, 0, true, true, false, false);
		concreteGameTypeTwentyHundredUlti.getGameTypeList().add(twentyHundred);
		concreteGameTypeTwentyHundredUlti.getGameTypeList().add(ulti);
		gameTypeMap.put(20, concreteGameTypeTwentyHundredUlti);

		final ConcreteGameType concreteGameTypeRedDurchmars = new ConcreteGameType(
				"Piros Durchmars", 12, 0, true, true, true, false);
		concreteGameTypeRedDurchmars.getGameTypeList().add(durchmars);
		gameTypeMap.put(22, concreteGameTypeRedDurchmars);

		final ConcreteGameType concreteGameTypeFortyHundredUltiDurchmars = new ConcreteGameType(
				"40-100 Ulti Durchmars", 14, 0, true, true, false, false);
		concreteGameTypeFortyHundredUltiDurchmars.getGameTypeList().add(
				fortyHundred);
		concreteGameTypeFortyHundredUltiDurchmars.getGameTypeList().add(ulti);
		concreteGameTypeFortyHundredUltiDurchmars.getGameTypeList().add(
				durchmars);
		gameTypeMap.put(24, concreteGameTypeFortyHundredUltiDurchmars);

		final ConcreteGameType concreteGameTypeTwentyHundredDurchmars = new ConcreteGameType(
				"20-100 Durchmars", 14, 0, true, true, false, false);
		concreteGameTypeTwentyHundredDurchmars.getGameTypeList().add(
				twentyHundred);
		concreteGameTypeTwentyHundredDurchmars.getGameTypeList().add(durchmars);
		gameTypeMap.put(25, concreteGameTypeTwentyHundredDurchmars);

		final ConcreteGameType concreteGameTypeRedFortyHundredUlti = new ConcreteGameType(
				"Piros 40-100 Ulti", 16, 0, true, true, true, false);
		concreteGameTypeRedFortyHundredUlti.getGameTypeList().add(fortyHundred);
		concreteGameTypeRedFortyHundredUlti.getGameTypeList().add(ulti);
		gameTypeMap.put(26, concreteGameTypeRedFortyHundredUlti);

		final ConcreteGameType concreteGameTypeRedTwentyHundred = new ConcreteGameType(
				"Piros 20-100", 16, 0, true, true, true, false);
		concreteGameTypeRedTwentyHundred.getGameTypeList().add(twentyHundred);
		gameTypeMap.put(27, concreteGameTypeRedTwentyHundred);

		final ConcreteGameType concreteGameTypeTwentyHundredUltiDurchmars = new ConcreteGameType(
				"20-100 Ulti Durchmars", 18, 0, true, true, false, false);
		concreteGameTypeTwentyHundredUltiDurchmars.getGameTypeList().add(
				twentyHundred);
		concreteGameTypeTwentyHundredUltiDurchmars.getGameTypeList().add(ulti);
		concreteGameTypeTwentyHundredUltiDurchmars.getGameTypeList().add(
				durchmars);
		gameTypeMap.put(31, concreteGameTypeTwentyHundredUltiDurchmars);

		final ConcreteGameType concreteGameTypeRedFortyHundredDurchmars = new ConcreteGameType(
				"Piros 40-100 Durchmars", 20, 0, true, true, true, false);
		concreteGameTypeRedFortyHundredDurchmars.getGameTypeList().add(
				fortyHundred);
		concreteGameTypeRedFortyHundredDurchmars.getGameTypeList().add(
				durchmars);
		gameTypeMap.put(33, concreteGameTypeRedFortyHundredDurchmars);

		final ConcreteGameType concreteGameTypeRedUltiDurchmars = new ConcreteGameType(
				"Piros Ulti Durchmars", 20, 0, true, true, true, false);
		concreteGameTypeRedUltiDurchmars.getGameTypeList().add(ulti);
		concreteGameTypeRedUltiDurchmars.getGameTypeList().add(durchmars);
		gameTypeMap.put(34, concreteGameTypeRedUltiDurchmars);

		final ConcreteGameType concreteGameTypeRedTwentyHundredUlti = new ConcreteGameType(
				"Piros 20-100 Ulti", 24, 0, true, true, true, false);
		concreteGameTypeRedTwentyHundredUlti.getGameTypeList().add(
				twentyHundred);
		concreteGameTypeRedTwentyHundredUlti.getGameTypeList().add(ulti);
		gameTypeMap.put(39, concreteGameTypeRedTwentyHundredUlti);

		final ConcreteGameType concreteGameTypeRedFortyHundredUltiDurchmars = new ConcreteGameType(
				"Piros 40-100 Ulti Durchmars", 28, 0, true, true, true, false);
		concreteGameTypeRedFortyHundredUltiDurchmars.getGameTypeList().add(
				fortyHundred);
		concreteGameTypeRedFortyHundredUltiDurchmars.getGameTypeList()
				.add(ulti);
		concreteGameTypeRedFortyHundredUltiDurchmars.getGameTypeList().add(
				durchmars);
		gameTypeMap.put(41, concreteGameTypeRedFortyHundredUltiDurchmars);

		final ConcreteGameType concreteGameTypeRedTwentyHundredDurchmars = new ConcreteGameType(
				"Piros 20-100 Durchmars", 28, 0, true, true, true, false);
		concreteGameTypeRedTwentyHundredDurchmars.getGameTypeList().add(
				twentyHundred);
		concreteGameTypeRedTwentyHundredDurchmars.getGameTypeList().add(
				durchmars);
		gameTypeMap.put(42, concreteGameTypeRedTwentyHundredDurchmars);

		final ConcreteGameType concreteGameTypeRedTwentyHundredUltiDurchmars = new ConcreteGameType(
				"Piros 20-100 Ulti Durchmars", 36, 0, true, true, true, false);
		concreteGameTypeRedTwentyHundredUltiDurchmars.getGameTypeList().add(
				twentyHundred);
		concreteGameTypeRedTwentyHundredUltiDurchmars.getGameTypeList().add(
				ulti);
		concreteGameTypeRedTwentyHundredUltiDurchmars.getGameTypeList().add(
				durchmars);
		gameTypeMap.put(45, concreteGameTypeRedTwentyHundredUltiDurchmars);
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
