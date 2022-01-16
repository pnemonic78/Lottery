package com.github.pnemonic.game.lottery.lotto;

import com.github.pnemonic.game.NumberStatistic;
import com.github.pnemonic.game.lottery.LotteryResultsReader;
import com.github.pnemonic.game.lottery.LotteryStats;

import java.io.File;

/**
 * Lotto statistics.
 * 
 * @author Moshe
 */
public class LottoStats extends LotteryStats {

	/**
	 * Constructs a new Lotto statistics.
	 */
	public LottoStats(Lotto lottery) {
		super(lottery, 6);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		String fileName = (args.length == 0) ? "results/Lotto.csv" : args[0];
		File file = new File(fileName);
		LottoStats stats = new LottoStats(new Lotto());
		stats.parse(file);
	}

	@Override
	protected LotteryResultsReader createResultsReader() {
		return new LottoResultsReader();
	}

	protected void printNumberStats() {
		System.out.println("max. repeat: " + maxRepeat);

		// NumberStatistic nstat;
		NumberStatistic[] nstatRow;
		// System.out.print("seq");
		// for (int n = 1; n <= 37; n++) {
		// System.out.print("\t");
		// System.out.print(n);
		// }
		// System.out.println();
		// for (int row = 0; row < numStats.length; row++) {
		// nstatRow = numStats[row];
		// nstat = nstatRow[0];
		// System.out.print(recStats.get(row).record.sequence);
		// for (int col = 0; col < nstatRow.length; col++) {
		// nstat = nstatRow[col];
		// System.out.print("\t");
		// System.out.print(nstat.count);
		// }
		// System.out.println();
		// }

		final int THRESHOLD_CANDIDATES_PERCENT = 50;
		final int thresholdCandidates = (numBalls * THRESHOLD_CANDIDATES_PERCENT) / 100;
		final int thresholdCandidatesAnd = Math.min((thresholdCandidates * 3) / 2, numBalls / 2);
		final int thresholdCandidatesOr = thresholdCandidates / 2;
		final int thresholdCandidatesOr3 = thresholdCandidates / 3;
		final int thresholdCandidatesOr4 = thresholdCandidates / 4;
		final int lotteryMin = lottery.getMinimum();
		final int lotteryMax = lottery.getMaximum();
		nstatRow = numStats[numStats.length - 1];
		StringBuffer asJava = new StringBuffer();
		int i = 0;
		boolean add;
		for (NumberStatistic nstat : nstatRow) {
			System.out.println(nstat);
			add = (nstat.id >= lotteryMin) && (nstat.id <= lotteryMax);
			add &= nstat.repeat < maxRepeat;
			// Copy from Tester#nextCandidates and paste here:
			add &= (nstat.indexLeastCount < thresholdCandidatesAnd) && (nstat.indexMostUsed < thresholdCandidatesAnd);
			if (add) {
				if (asJava.length() > 0) {
					asJava.append(',');
				}
				asJava.append(nstat.id);
				i++;
			}
		}
		System.out.println(asJava);
	}

}
