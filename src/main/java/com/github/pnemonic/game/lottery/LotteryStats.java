package com.github.pnemonic.game.lottery;

import com.github.pnemonic.game.CompareByCount;
import com.github.pnemonic.game.CompareByUsage;
import com.github.pnemonic.game.CompareNumber;
import com.github.pnemonic.game.NumberStatistic;
import com.github.pnemonic.game.RecordStatistic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class LotteryStats {

	protected final Lottery lottery;
	protected List<LotteryRecord> records;
	protected final List<com.github.pnemonic.game.RecordStatistic> recStats = new ArrayList<com.github.pnemonic.game.RecordStatistic>();
	protected NumberStatistic[][] numStats;
	/** The upper bound of the minimum balls. */
	protected int maxLower;
	/** The lower bound of the maximum balls. */
	protected int minUpper;
	/** The widest gap between any 2 balls. */
	protected int maxGap;
	protected final int lotteryMin;
	protected final int lotteryMax;
	/** The number of balls. */
	protected final int numBalls;
	/** The lowest number of odd balls. */
	protected int minOdd;
	/** The lowest number of even balls. */
	protected int minEven;
	/** The highest number of odd balls. */
	protected int maxOdd;
	/** The highest number of even balls. */
	protected int maxEven;
	/** The highest number of repeated balls. */
	protected int maxRepeat;
	/** The number of balls per record. */
	protected final int numRecordBalls;
	/** The number of occurrences of pairs. */
	protected int[][] numPairs;

	public LotteryStats(Lottery lottery, int numRecordBalls) {
		super();
		this.lottery = lottery;
		this.lotteryMin = lottery.getMinimum();
		this.lotteryMax = lottery.getMaximum();
		this.numBalls = lottery.getNumberBalls();
		this.numRecordBalls = numRecordBalls;
	}

	protected abstract LotteryResultsReader createResultsReader();

	public void parse(File file) {
		LotteryResultsReader reader = createResultsReader();
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
		numPairs = new int[numBalls][numBalls];

		com.github.pnemonic.game.RecordStatistic rstat;
		NumberStatistic nstat, nstatPrev;
		NumberStatistic[] numStatsRow;
		NumberStatistic[] numStatsRow_1;
		NumberStatistic[] numStatsRowSorted = new NumberStatistic[numBalls];
		final CompareNumber forLeastCount = new CompareByCount(false);
		final CompareNumber forLeastUsed = new CompareByUsage(false);

		int row = 0;
		int row_1;
		int col;
		for (LotteryRecord record : records) {
			numStatsRow = numStats[row];
			row_1 = row - 1;

			if (processRecordStatistics) {
				maxLower = Math.max(maxLower, record.lot[0]);
				for (int i = numRecordBalls - 1; i > 0; i--) {
					int n = record.lot[i];
					if ((n >= lotteryMin) && (n <= lotteryMax)) {
						minUpper = Math.min(minUpper, n);
						break;
					}
				}

				rstat = addStat(record);
				if (rstat == null) {
					throw new NullPointerException();
				}

				for (int i = 0; i < rstat.gap.length; i++) {
					int gap = rstat.gap[i];
					int n0 = record.lot[i];
					int n1 = n0 + gap;
					if ((n0 >= lotteryMin) && (n0 <= lotteryMax) && (n1 >= lotteryMin) && (n1 <= lotteryMax)) {
						maxGap = Math.max(maxGap, gap);
					}
				}
			}

			if (processNumberStatitics) {
				col = 0;
				for (int n = 1; n <= numBalls; col++, n++) {
					nstat = new NumberStatistic();
					nstat.id = n;
					numStatsRow[col] = nstat;
				}

				int col2, pair, pair2;
				int pairIndex, pairIndex2;
				for (col = 0; col < record.lot.length; col++) {
					pair = record.lot[col];
					if ((pair >= lotteryMin) && (pair <= lotteryMax)) {
						pairIndex = pair - lotteryMin;
						col2 = col + 1;

						if (col2 < record.lot.length) {
							pair2 = record.lot[col2];
							if ((pair2 >= lotteryMin) && (pair2 <= lotteryMax)) {
								pairIndex2 = pair2 - lotteryMin;
								numPairs[pairIndex][pairIndex2]++;
								numPairs[pairIndex2][pairIndex]++;
							}
						}
					}
				}

				for (int n : record.lot) {
					if ((n >= lotteryMin) && (n <= lotteryMax)) {
						col = n - lotteryMin;
						nstat = numStatsRow[col];
						nstat.occur++;
					}
				}

				if (row_1 >= 0) {
					numStatsRow_1 = numStats[row_1];

					col = 0;
					for (int n = 1; n <= numBalls; col++, n++) {
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
					col = 0;
					for (int n = 1; n <= numBalls; col++, n++) {
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
				col = 0;
				for (int n = 1; n <= numBalls; col++, n++) {
					nstat = numStatsRowSorted[col];
					nstat.indexLeastCount = col;
					nstat.indexMostCount = numBalls - col - 1;
				}

				Arrays.sort(numStatsRowSorted, forLeastUsed);
				col = 0;
				for (int n = 1; n <= numBalls; col++, n++) {
					nstat = numStatsRowSorted[col];
					nstat.indexLeastUsed = col;
					nstat.indexMostUsed = numBalls - col - 1;
				}
			}
			row++;
		}
	}

	protected com.github.pnemonic.game.RecordStatistic addStat(LotteryRecord record) {
		final int size = numRecordBalls;
		final int sizeConsecutives = size - 1;
		com.github.pnemonic.game.RecordStatistic rstat = new com.github.pnemonic.game.RecordStatistic(sizeConsecutives);
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
