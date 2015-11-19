package interfaces;

import domain.Player;

public interface IPlayerStatisticRepository {

	public void savePoints(final Player player, final int points);
}
