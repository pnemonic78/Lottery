package com.github.pnemonic.game.lottery;

import com.github.pnemonic.game.Statistic;

public class RecordStatistic extends Statistic {

	public LotteryRecord record;

	/** Number of <tt>index + 2</tt> consecutives. */
	public final int[] consecutive;

	/** Gap between ball at <tt>index</tt> and ball at <tt>index + 1</tt>. */
	public final int[] gap;

	public RecordStatistic(int size) {
		super();
		this.consecutive = new int[size];
		this.gap = new int[size];
	}

}
