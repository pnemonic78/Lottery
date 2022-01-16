package game.lottery.lotto;

import java.util.Set;

import game.lottery.LotteryGame;



/**
 * Choose numbers for Double Lotto for Israel.
 * 
 * @author Moshe
 */
public class DoubleLotto extends Lotto {

	/**
	 * Constructs a new Lotto.
	 */
	public DoubleLotto() {
		super();
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            the array of arguments.
	 */
	public static void main(String[] args) {
		DoubleLotto lotto = new DoubleLotto();
		double budget = 100.00;
		double cost = COST * 2; // Double Lotto costs 2x
		int plays = (int) Math.floor(budget / cost);
		plays &= ~1; // Lotto played in pairs.
		Set<LotteryGame> games = lotto.play(plays);
		for (LotteryGame game : games) {
			lotto.print(game);
		}
	}

}
