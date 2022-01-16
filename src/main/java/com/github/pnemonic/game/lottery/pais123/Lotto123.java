package com.github.pnemonic.game.lottery.pais123;

import com.github.pnemonic.game.lottery.Lottery;
import com.github.pnemonic.game.lottery.LotteryGame;

import java.util.Set;

/**
 * Choose numbers for 123 for Israel.
 * 
 * @author Moshe
 */
public class Lotto123 extends Lottery {

	/** Cost per game. */
	public static final double BUDGET = 10;
	/** Cost per game. */
	public static final double COST = 1;
	/** Total number of plays per budget. */
	public static final int PLAYS = (int) Math.floor(BUDGET / COST);

	private static final int SIZE = 1;

	/**
	 * Constructs a new 123.
	 */
	public Lotto123() {
		super();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		Lotto123 lottery = new Lotto123();
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
		return 0;
	}

	@Override
	public int getMaximum() {
		return 9;
	}

	@Override
	public int getBonusMinimum() {
		return 0;
	}

	@Override
	public int getBonusMaximum() {
		return 0;
	}

}
