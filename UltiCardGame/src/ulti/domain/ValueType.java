package ulti.domain;

public class ValueType {
	public static enum Value {
		ACE(1), TEN(5), KING(2), OVER_KNAVE(3), UNDER_KNAVE(4), NINE(6), EIGHT(
				7), SEVEN(8);

		private final int val;

		private Value(final int v) {
			val = v;
		}

		public int getVal(final boolean isTenUp) {
			if (!isTenUp) {
				return val;
			} else {
				return ordinal();
			}
		}

		public Value[] getValues(final boolean isTenUp) {
			if (!isTenUp) {
				return new Value[] { ACE, KING, OVER_KNAVE, UNDER_KNAVE, TEN,
						NINE, EIGHT, SEVEN };
			} else {
				return values();
			}
		}
	}

}
