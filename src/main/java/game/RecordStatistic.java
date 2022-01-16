package game;

import game.lottery.LotteryRecord;

public class RecordStatistic extends Statistic {

	public LotteryRecord record;

	/** Number of <tt>index + 2</tt> consecutives. */
	public final int[] consecutive;

	/** Gap between ball at <tt>index</tt> and ball at <tt>index + 1</tt>. */
	public final int[] gap;

	/** Number of odd-numbered balls. */
	public int odd;
	/** Number of even-numbered balls. */
	public int even;

	public RecordStatistic(int size) {
		super();
		this.consecutive = new int[size];
		this.gap = new int[size];
	}

}
