package com.github.pnemonic.game.lottery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * Lottery.
 * 
 * @author Moshe
 */
public abstract class Lottery {

	protected static final Random rnd = new Random();

	private final Set<Integer> candidates = new TreeSet<Integer>();

	private static final int RETRIES = 10;

	protected final int bonusMinimum;
	protected final int bonusMaximum;

	/**
	 * Constructs a new lottery.
	 */
	public Lottery() {
		super();
		bonusMinimum = getBonusMinimum();
		bonusMaximum = getBonusMaximum();
		setCandidates((int[]) null);
	}

	public abstract int size();

	public abstract int getMinimum();

	public abstract int getMaximum();

	public abstract int getBonusMinimum();

	public abstract int getBonusMaximum();

	public int getNumberBalls() {
		return getMaximum() - getMinimum() + 1;
	}

	/**
	 * Choose some numbers.
	 * <p>
	 * Populate the bag of numbers to choose from, and remove 6 candidates.
	 * 
	 * @return the lot of chosen numbers - <tt>null</tt> if game is invalid.
	 */
	public LotteryGame play() {
		int size = size();
		List<Integer> bag = createBag();
		LotteryGame game = new LotteryGame(size);
		int index, candidate;
		for (int pick = 0; pick < size; pick++) {
			if (bag.isEmpty()) {
				break;
			}
			index = rnd.nextInt(bag.size());
			candidate = bag.remove(index);
			game.lot[pick] = candidate;
			Arrays.sort(game.lot, 0, pick + 1);
			filter(game, pick, bag);
		}
		playBonus(game);
		filterGame(game);
		return game;
	}

	/**
	 * Print the chosen numbers.
	 * 
	 * @param play
	 *            the play number.
	 * @param lot
	 *            the chosen numbers.
	 */
	public void print(LotteryGame game) {
		System.out.print("Play: " + game.id);
		System.out.print("\tLotto:");
		for (Integer i : game.lot) {
			System.out.print("\t" + i);
		}
		if (game.bonus > 0) {
			System.out.print("\tBonus: " + game.bonus);
		}
		System.out.println();
	}

	/**
	 * Filter the bag by applying various rules of probability.
	 * 
	 * @param game
	 *            the game.
	 * @param pickIndex
	 *            the number index.
	 * @param bag
	 *            the bag of candidate numbers.
	 */
	protected void filter(LotteryGame game, int pickIndex, List<Integer> bag) {
	}

	/**
	 * Filter the game by applying various rules of probability.
	 * 
	 * @param game
	 *            the game.
	 * @param pickIndex
	 *            the number index.
	 * @param bag
	 *            the bag of candidate numbers.
	 * @throws LotException
	 *             if the game's lot is invalid.
	 */
	protected void filterGame(LotteryGame game) throws LotException {
	}

	public Set<LotteryGame> play(int numGames) {
		if (numGames <= 0) {
			throw new IllegalArgumentException("Invalid number of games " + numGames);
		}
		Set<LotteryGame> games = new TreeSet<LotteryGame>();
		LotteryGame game;
		int play = 1;
		int retry = 0;

		while (games.size() < numGames) {
			try {
				game = play();
				game.id = play++;
				games.add(game);
			} catch (LotException le) {
				// TODO System.err.println(le.getMessage());
				retry++;
				if (retry >= RETRIES) {
					break;
				}
			}
		}
		play = 1;
		for (LotteryGame g : games) {
			g.id = play++;
		}
		return games;
	}

	protected List<Integer> createBag() {
		List<Integer> bag = new ArrayList<Integer>(candidates);
		// Collections.sort(bag);
		return bag;
	}

	public void setCandidates(int[] candidates) {
		this.candidates.clear();
		if ((candidates == null) || (candidates.length < size())) {
			int min = getMinimum();
			int max = getMaximum();
			for (int n = min; n <= max; n++) {
				this.candidates.add(n);
			}
		} else {
			for (int n : candidates) {
				this.candidates.add(n);
			}
		}
	}

	public void setCandidates(Collection<Integer> candidates) {
		this.candidates.clear();
		if ((candidates == null) || (candidates.size() < size())) {
			int min = getMinimum();
			int max = getMaximum();
			for (int n = min; n <= max; n++) {
				this.candidates.add(n);
			}
		} else {
			this.candidates.addAll(candidates);
		}
	}

	public void setCandidates(String candidates) {
		String[] tokens = candidates.split(",");
		Set<Integer> balls = new TreeSet<Integer>();
		for (String token : tokens) {
			balls.add(Integer.valueOf(token));
		}
		setCandidates(balls);
	}

	protected void playBonus(LotteryGame game) {
		if (bonusMinimum < bonusMaximum) {
			List<Integer> bag = new ArrayList<Integer>();
			for (int n = bonusMinimum; n <= bonusMaximum; n++) {
				bag.add(n);
			}
			int index = rnd.nextInt(bag.size());
			int candidate = bag.remove(index);
			game.bonus = candidate;
		} else {
			game.bonus = bonusMinimum;
		}
	}
}
