package game;

import java.util.Comparator;

public abstract class CompareNumber implements Comparator<NumberStatistic> {

	protected final boolean descending;

	public CompareNumber() {
		this(false);
	}

	public CompareNumber(boolean descending) {
		super();
		this.descending = descending;
	}

}
