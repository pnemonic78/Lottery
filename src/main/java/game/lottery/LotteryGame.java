package game.lottery;

import java.util.Arrays;

/**
 * Lottery game.
 * 
 * @author Moshe
 */
public class LotteryGame implements Comparable<LotteryGame> {

	public int id;
	public int[] lot;
	public int bonus;

	/**
	 * Constructs a new game.
	 */
	public LotteryGame() {
		super();
	}

	/**
	 * Constructs a new game.
	 */
	public LotteryGame(int size) {
		super();
		this.lot = new int[size];
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LotteryGame) {
			LotteryGame that = (LotteryGame) obj;
			return Arrays.equals(this.lot, that.lot) && (this.bonus == that.bonus);
		}
		return super.equals(obj);
	}

	public int compareTo(LotteryGame that) {
		int size0 = this.lot.length;
		int size1 = that.lot.length;
		int comp = size0 - size1;
		if (comp == 0) {
			int[] lot0 = this.lot;
			int[] lot1 = that.lot;
			for (int i = 0; (i < size0) && (comp == 0); i++) {
				comp = lot0[i] - lot1[i];
			}

			if (comp == 0) {
				comp = this.bonus - that.bonus;
				if (comp == 0) {
					comp = this.id - that.id;
				}
			}
		}
		return comp;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(id).append(": ");
		for (int ball : lot) {
			buf.append(ball).append(' ');
		}
		buf.append('+').append(bonus);
		return buf.toString();
	}
}
