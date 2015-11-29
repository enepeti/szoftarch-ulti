package ulti.domain.gametype;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class GameTypeConverter {

	private static final Map<Integer, ConcreteGameType> gameTypeMap = new HashMap<Integer, ConcreteGameType>();

	public GameTypeConverter() {
		final ConcreteGameType concreteGameTypeParty = new ConcreteGameType(
				"Parti", 0, 1, true, true, false, true);
		concreteGameTypeParty.getGameTypeList().add(new GameTypeParty());
		gameTypeMap.put(1, concreteGameTypeParty);

		final ConcreteGameType concreteGameTypeRedParty = new ConcreteGameType(
				"Piros Parti", 0, 2, true, true, true, true);
		concreteGameTypeRedParty.getGameTypeList().add(new GameTypeParty());
		gameTypeMap.put(2, concreteGameTypeRedParty);

		final ConcreteGameType concreteGameTypeFortyHundred = new ConcreteGameType(
				"40-100", 4, 0, true, true, false, false);
		concreteGameTypeFortyHundred.getGameTypeList().add(
				new GameTypeFortyHundred());
		gameTypeMap.put(3, concreteGameTypeFortyHundred);

		final ConcreteGameType concreteGameTypeUlti = new ConcreteGameType(
				"Ulti", 4, 1, true, true, false, true);
		concreteGameTypeUlti.getGameTypeList().add(new GameTypeUlti());
		gameTypeMap.put(4, concreteGameTypeUlti);
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
