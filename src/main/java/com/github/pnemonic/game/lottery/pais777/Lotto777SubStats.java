package com.github.pnemonic.game.lottery.pais777;

import com.github.pnemonic.game.lottery.Lottery;

import java.io.File;

public class Lotto777SubStats extends Lotto777Stats {

	public Lotto777SubStats(Lottery lottery) {
		super(lottery);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String fileName = (args.length == 0) ? "results/777.csv" : args[0];
		File file = new File(fileName);
		Lotto777Stats stats = new Lotto777Stats(new Lotto777Sub());
		stats.parse(file);
	}

}
