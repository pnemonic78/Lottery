package com.github.pnemonic.game.lottery.pais777;

import com.github.pnemonic.game.lottery.LotException;
import com.github.pnemonic.game.lottery.Lottery;
import com.github.pnemonic.game.lottery.LotteryGame;

import java.util.List;
import java.util.Set;

/**
 * Choose numbers for 777 for Israel.
 * 
 * @author Moshe
 */
public class Lotto777 extends Lottery {

	/** Number of groupings to play. */
	private static final int GROUPINGS = 1;
	/** Cost per game. */
	public static final double COST = 7.00;
	/** Budget. */
	public static final double BUDGET = 200.00 / GROUPINGS;
	/** Total number of plays per budget. */
	public static final int PLAYS = (int) Math.floor(BUDGET / COST);

	private static final int SIZE = 7;
	private static final int MAX_LOWER = 23;
	private static final int MIN_UPPER = 43;
	private static final int MAX_GAP = 32;
	private static final int MIN_ODD = 2;
	private static final int MIN_EVEN = 2;

	/**
	 * Constructs a new 777.
	 */
	public Lotto777() {
		super();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		Lottery lottery = new Lotto777();
		if (args.length > 0) {
			lottery.setCandidates(args[0]);
		}

		int plays = PLAYS;
		Set<LotteryGame> games = lottery.play(plays);
		for (LotteryGame game : games) {
			lottery.print(game);
		}
	}

	@Override
	public int size() {
		return SIZE;
	}

	@Override
	public int getMinimum() {
		return 1;
	}

	@Override
	public int getMaximum() {
		return 70;
	}

	@Override
	public int getBonusMinimum() {
		return 0;
	}

	@Override
	public int getBonusMaximum() {
		return 0;
	}

	@Override
	protected void filter(LotteryGame game, int pickIndex, List<Integer> bag) throws LotException {
		super.filter(game, pickIndex, bag);

		// Rule: Too few "5 consecutive" numbers.
		if (pickIndex >= 4) {
			int[] lot = game.lot;
			int candidate;

			if (pickIndex >= 4) {
				int i = pickIndex;
				if ((lot[i - 4] + 1 == lot[i - 3]) && (lot[i - 3] + 1 == lot[i - 2]) && (lot[i - 2] + 1 == lot[i - 1]) && (lot[i - 1] + 1 == lot[i])) {
					// remove 5th consecutive numbers: 1 before; 1 after.
					candidate = lot[i - 4] - 1;
					bag.remove((Object) candidate);
					candidate = lot[i] + 1;
					bag.remove((Object) candidate);
				}
			}
		}
	}

	@Override
	protected void filterGame(LotteryGame game) throws LotException {
		super.filterGame(game);
		int candidate;
		int[] lot = game.lot;
		int size = lot.length;

		// Rule: Minimum-valued ball cannot exceed MAX_LOWER.
		candidate = lot[0];
		if (candidate > MAX_LOWER) {
			throw new LotException("Minimum-valued ball " + candidate + " exceeds " + MAX_LOWER);
		}
		// Rule: Maximum-valued ball cannot precede MIN_UPPER.
		candidate = lot[size - 1];
		if (candidate < MIN_UPPER) {
			throw new LotException("Maximum-valued ball " + candidate + " less than " + MIN_UPPER);
		}

		// Rule: Biggest gap between 2 balls.
		for (int l = 0, l1 = 1; l1 < size; l++, l1++) {
			if (lot[l] + MAX_GAP <= lot[l1]) {
				throw new LotException("Widest gap between " + lot[l] + " and " + lot[l1] + " exceeds " + MAX_GAP);
			}
		}

		// Rule: Minimum number of odd-numbered balls.
		int countOdd = 0;
		int countEven = 0;
		for (int l : lot) {
			if ((l & 1) == 0) {
				countEven++;
			} else {
				countOdd++;
			}
		}
		if (countOdd < MIN_ODD) {
			throw new LotException("Minimum odds less than " + MIN_ODD);
		}
		if (countEven < MIN_EVEN) {
			throw new LotException("Minimum evens less than " + MIN_EVEN);
		}
	}

}
