package com.github.pnemonic.game;

public class NumberStatistic extends Statistic implements Comparable<NumberStatistic> {
	/** The ball value. */
	public int id;
	/** Number of occurrences of this ball in the game. */
	public int occur;
	/** Total number of occurrences. */
	public int count;
	/** Usage (age). */
	public float usage;
	/**
	 * Total number of repetitions of consecutive games. Related to
	 * {@link #usage}.
	 */
	public int repeat;
	/** Maximum number of repetitions of consecutive games. */
	public int maxRepeat;
	/** Index when sorted by "least count". */
	public int indexLeastCount;
	/** TODO comment me! */
	public int indexMostCount;
	/** TODO comment me! */
	public int indexLeastUsed;
	/** TODO comment me! */
	public int indexMostUsed;
	/** TODO comment me! */
	public int indexLeastRepeat;
	/** TODO comment me! */
	public int indexMostRepeat;

	public int compareTo(NumberStatistic that) {
		int comp = (int) ((this.usage - that.usage) * 1e+8);
		if (comp == 0) {
			comp = this.id - that.id;
		}
		return comp;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(id);
		buf.append('\t').append(count);
		buf.append('\t').append(usage);
		buf.append('\t').append(repeat);
		buf.append('\t').append(maxRepeat);
		return buf.toString();
	}
}
