package game.lottery.pais123;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.CompareByCount;
import game.CompareByUsage;
import game.CompareNumber;
import game.NumberStatistic;
import game.RecordStatistic;
import game.lottery.Lottery;
import game.lottery.LotteryRecord;
import game.lottery.LotteryResultsReader;

/**
 * 123 statistics.
 * 
 * @author Moshe
 */
public class Lotto123Stats {

	private List<LotteryRecord> records;
	private final List<RecordStatistic> recStats = new ArrayList<RecordStatistic>();
	private NumberStatistic[][] numStats;
	/** The upper bound of the minimum balls. */
	private int maxLower;
	/** The lower bound of the maximum balls. */
	private int minUpper;
	/** The widest gap between any 2 balls. */
	private int maxGap;
	/** The number of balls. */
	private final int numBalls;
	/** The lowest number of odd balls. */
	private int minOdd;
	/** The lowest number of even balls. */
	private int minEven;
	/** The highest number of odd balls. */
	private int maxOdd;
	/** The highest number of even balls. */
	private int maxEven;
	/** The highest number of repeated balls. */
	private int maxRepeat;
	/** The number of balls per record. */
	private final int numRecordBalls;

	/**
	 * Constructs a new 123 statistics.
	 * 
	 * @param lottery
	 *            the lottery.
	 */
	public Lotto123Stats(Lottery lottery) {
		super();
		this.numBalls = lottery.getNumberBalls();
		this.numRecordBalls = 3;
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		String fileName = (args.length == 0) ? "results/123.csv" : args[0];
		File file = new File(fileName);
		Lotto123Stats stats = new Lotto123Stats(new Lotto123());
		stats.parse(file);
	}

	public void parse(File file) {
		LotteryResultsReader reader = new Lotto123ResultsReader();
		try {
			records = reader.parse(file);
			processRecords(records);
			printRecordStats();
			printNumberStats();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void processRecords(List<LotteryRecord> records) {
		processRecords(records, true, true);
	}

	public void processRecords(List<LotteryRecord> records, boolean processRecordStatistics, boolean processNumberStatitics) {
		recStats.clear();
		numStats = new NumberStatistic[records.size()][numBalls];
		maxLower = Integer.MIN_VALUE;
		minUpper = Integer.MAX_VALUE;
		maxGap = 0;
		minOdd = Integer.MAX_VALUE;
		maxOdd = 0;
		minEven = Integer.MAX_VALUE;
		maxEven = 0;
		maxRepeat = 0;

		RecordStatistic rstat;
		NumberStatistic nstat, nstatPrev;
		NumberStatistic[] numStatsRow;
		NumberStatistic[] numStatsRow_1;
		NumberStatistic[] numStatsRowSorted = new NumberStatistic[numBalls];
		final CompareNumber forLeastCount = new CompareByCount(false);
		final CompareNumber forLeastUsed = new CompareByUsage(false);

		int row = 0;
		int row_1;
		for (LotteryRecord record : records) {
			numStatsRow = numStats[row];
			row_1 = row - 1;

			if (processRecordStatistics) {
				for (int ball : record.lot) {
					maxLower = Math.max(maxLower, ball);
					minUpper = Math.min(minUpper, ball);
				}

				rstat = addStat(record);
				if (rstat == null) {
					throw new NullPointerException();
				}

				for (int gap : rstat.gap) {
					maxGap = Math.max(maxGap, gap);
				}
			}

			if (processNumberStatitics) {
				for (int col = 0, n = 1; n <= numBalls; col++, n++) {
					nstat = new NumberStatistic();
					nstat.id = n;
					numStatsRow[col] = nstat;
				}

				for (int n : record.lot) {
					nstat = numStatsRow[n - 1];
					nstat.occur++;
				}

				if (row_1 >= 0) {
					numStatsRow_1 = numStats[row_1];

					for (int col = 0, n = 1; n <= numBalls; col++, n++) {
						nstat = numStatsRow[col];
						nstatPrev = numStatsRow_1[col];
						nstat.count = nstatPrev.count + nstat.occur;
						nstat.usage = (nstatPrev.usage + nstat.occur) / 2;
						if (nstat.occur > 0) {
							nstat.repeat = nstatPrev.repeat + nstat.occur;
						}
						nstat.maxRepeat = Math.max(nstat.repeat, nstatPrev.maxRepeat);
						maxRepeat = Math.max(maxRepeat, nstat.maxRepeat);
					}
				} else {
					for (int col = 0, n = 1; n <= numBalls; col++, n++) {
						nstat = numStatsRow[col];
						nstat.count += nstat.occur;
						nstat.usage = nstat.occur;
						nstat.repeat = nstat.occur;
						nstat.maxRepeat = Math.max(nstat.repeat, nstat.maxRepeat);
						maxRepeat = Math.max(maxRepeat, nstat.maxRepeat);
					}
				}

				System.arraycopy(numStatsRow, 0, numStatsRowSorted, 0, numBalls);
				Arrays.sort(numStatsRowSorted, forLeastCount);
				for (int col = 0, n = 1; n <= numBalls; col++, n++) {
					nstat = numStatsRowSorted[col];
					nstat.indexLeastCount = col;
					nstat.indexMostCount = numBalls - col - 1;
				}

				Arrays.sort(numStatsRowSorted, forLeastUsed);
				for (int col = 0, n = 1; n <= numBalls; col++, n++) {
					nstat = numStatsRowSorted[col];
					nstat.indexLeastUsed = col;
					nstat.indexMostUsed = numBalls - col - 1;
				}
			}
			row++;
		}
	}

	protected RecordStatistic addStat(LotteryRecord record) {
		final int size = numRecordBalls;
		final int sizeConsecutives = size - 1;
		RecordStatistic rstat = new RecordStatistic(sizeConsecutives);
		rstat.record = record;

		boolean[] con = new boolean[sizeConsecutives];
		for (int i = 0, i1 = 1; i1 <= con.length; i++, i1++) {
			con[i] = record.lot[i] + 1 == record.lot[i1];
			rstat.gap[i] = record.lot[i1] - record.lot[i];
		}

		boolean c;
		for (int i = 0, i1 = 1; i1 < size; i++, i1++) {
			c = true;
			for (int j = i, j1 = j + 1, s = 0; c && (j1 < size); j++, j1++, s++) {
				c &= con[j];
				rstat.consecutive[s] += c ? 1 : 0;
			}
		}

		for (int ball : record.lot) {
			if ((ball & 1) == 0) {
				rstat.even++;
			} else {
				rstat.odd++;
			}
		}
		minOdd = Math.min(minOdd, rstat.odd);
		maxOdd = Math.max(maxOdd, rstat.odd);
		minEven = Math.min(minEven, rstat.even);
		maxEven = Math.max(maxEven, rstat.even);

		recStats.add(rstat);

		return rstat;
	}

	protected void printRecordStats() {
		System.out.println("max. lower: " + maxLower);
		System.out.println("min. upper: " + minUpper);
		System.out.println("max. gap: " + maxGap);
		System.out.println("min. odd: " + minOdd);
		System.out.println("max. odd: " + maxOdd);
		System.out.println("min. even: " + minEven);
		System.out.println("max. even: " + maxEven);

		for (RecordStatistic rstat : recStats) {
			System.out.print(rstat.record.id);
			for (int c : rstat.consecutive) {
				System.out.print("\t" + c);
			}
			System.out.print("\te:" + rstat.even);
			System.out.print("\to:" + rstat.odd);
			System.out.println();
		}
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

		final int threshold = 35;
		nstatRow = numStats[numStats.length - 1];
		StringBuffer asJava = new StringBuffer();
		int i = 0;
		for (NumberStatistic stat : nstatRow) {
			System.out.println(stat);
			if ((stat.repeat < maxRepeat) && (stat.indexMostCount < threshold) && (stat.indexLeastUsed < threshold)) {
				if (asJava.length() > 0) {
					asJava.append(',');
				}
				asJava.append(stat.id);
				i++;
			}
		}
		System.out.println(asJava);
	}

	/**
	 * Get the number statistics. Each row represents a game. Each column
	 * represents a ball.
	 * 
	 * @return
	 */
	public NumberStatistic[][] getNumberStatistics() {
		return numStats;
	}
}
