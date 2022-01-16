package com.github.pnemonic.game;

public class CompareByUsage extends CompareNumber {

	public CompareByUsage() {
		super();
	}

	public CompareByUsage(boolean descending) {
		super(descending);
	}

	public int compare(NumberStatistic o1, NumberStatistic o2) {
		int comp = (int) ((descending ? (o2.usage - o1.usage) : (o1.usage - o2.usage)) * 1e+9);
		if (comp == 0) {
			comp = descending ? (o2.id - o1.id) : (o1.id - o2.id);
		}
		return comp;
	}

}
