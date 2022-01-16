package game.lottery.pais777;

import java.io.File;

import game.NumberStatistic;
import game.lottery.Lottery;
import game.lottery.LotteryResultsReader;
import game.lottery.LotteryStats;

/**
 * 777 statistics.
 * 
 * @author Moshe
 */
public class Lotto777Stats extends LotteryStats {

	/**
	 * Constructs a new 777 statistics.
	 * 
	 * @param lottery
	 *            the lottery.
	 */
	public Lotto777Stats(Lottery lottery) {
		super(lottery, 17);
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		String fileName = (args.length == 0) ? "results/777.csv" : args[0];
		File file = new File(fileName);
		Lotto777Stats stats = new Lotto777Stats(new Lotto777());
		stats.parse(file);
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

		if (numPairs != null) {
			StringBuffer p;
			for (int i = 0; i < numPairs.length; i++) {
				p = new StringBuffer();
				int[] numPairsRow = numPairs[i];
				p.append('{');
				for (int count : numPairsRow) {
					p.append(count).append(',');
				}
				p.deleteCharAt(p.length() - 1);
				p.append('}').append(',');
				System.out.println(p);
			}
		}

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
			add &= (nstat.indexMostUsed < thresholdCandidatesOr4) || (nstat.indexLeastUsed < thresholdCandidatesOr4)
					|| (nstat.indexMostCount < thresholdCandidatesOr4) || (nstat.indexLeastCount < thresholdCandidatesOr4);
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

	@Override
	protected LotteryResultsReader createResultsReader() {
		return new Lotto777ResultsReader();
	}
}
