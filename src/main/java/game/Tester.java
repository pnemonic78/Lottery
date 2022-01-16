package game;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import game.lottery.Lottery;
import game.lottery.LotteryGame;
import game.lottery.LotteryRecord;

public abstract class Tester {

	protected final Lottery lottery;
	protected final int lotterySize;
	protected final int lotteryMin;
	protected final int lotteryMax;
	protected final int numBalls;

	protected List<LotteryRecord> records;
	protected int recordsSize;
	protected int numGamesTotal;
	protected NumberStatistic[][] numStats;

	/**
	 * Creates a new tester.
	 */
	public Tester(Lottery lottery) {
		super();
		this.lottery = lottery;
		this.lotterySize = lottery.size();
		this.lotteryMin = lottery.getMinimum();
		this.lotteryMax = lottery.getMaximum();
		this.numBalls = lottery.getNumberBalls();
	}

	public abstract void parse(File file) throws IOException;

	public abstract void drive();

	protected abstract void drive(NumberStatisticGrouping grouping, String name);

	protected Set<LotteryGame> play(int numGames) {
		Set<LotteryGame> games = lottery.play(numGames);
		int gamesSize = games.size();
		int retry = 10;
		while ((gamesSize < numGames) && (gamesSize > 1)) {
			games.addAll(lottery.play(numGames - gamesSize));
			gamesSize = games.size();
			retry--;
			if (retry == 0) {
				break;
			}
		}
		return games;
	}
}
