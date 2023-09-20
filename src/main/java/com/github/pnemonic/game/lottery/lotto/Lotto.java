package com.github.pnemonic.game.lottery.lotto;

import com.github.pnemonic.game.lottery.LotException;
import com.github.pnemonic.game.lottery.Lottery;
import com.github.pnemonic.game.lottery.LotteryGame;

import java.util.List;
import java.util.Set;

/**
 * Choose numbers for Double Lotto for Israel.
 *
 * @author Moshe
 */
public class Lotto extends Lottery {

    /**
     * Cost per game.
     */
    public static final double BUDGET = 100;
    /**
     * Cost per game.
     */
    public static final double COST = 2.90;
    /**
     * Total number of plays per budget. Lotto played in pairs.
     */
    public static final int PLAYS = (int) Math.floor(BUDGET / COST) & ~1;

    private static final int SIZE = 6;
    private static final int MAX_LOWER = 21;
    private static final int MIN_UPPER = 13;
    private static final int MAX_GAP = 28;

    /**
     * Constructs a new Lotto.
     */
    public Lotto() {
        super();
    }

    /**
     * Main method.
     *
     * @param args the array of arguments.
     */
    public static void main(String[] args) {
        Lottery lottery = new Lotto();
        if (args.length > 0) {
            lottery.setCandidates(args[0]);
        }

        int plays = PLAYS;
        Set<LotteryGame> games = lottery.play(plays);
        for (LotteryGame game : games) {
            lottery.print(game);
        }
    }

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public int getMinimum() {
        return 1;
    }

    @Override
    public int getMaximum() {
        return 37;
    }

    @Override
    public int getBonusMinimum() {
        return 1;
    }

    @Override
    public int getBonusMaximum() {
        return 8;
    }

    @Override
    protected void filter(LotteryGame game, int pickIndex, List<Integer> bag) throws LotException {
        super.filter(game, pickIndex, bag);
        int candidate;

        // Rule: Too few "more than triple" consecutive numbers.
        if (pickIndex >= 2) {
            int[] lot = game.lot;
            int size = lot.length;

            int i = pickIndex;
            if ((lot[i - 2] + 1 == lot[i - 1]) && (lot[i - 1] + 1 == lot[i])) {
                // remove 4th consecutive numbers: 1 before; 1 after.
                candidate = lot[i - 2] - 1;
                bag.remove((Object) candidate);
                candidate = lot[i] + 1;
                bag.remove((Object) candidate);
            }

            // Rule: No double triples.
            if (pickIndex >= 4) {
                if ((lot[0] + 1 == lot[1]) && (lot[1] + 1 == lot[2]) && (lot[3] + 1 == lot[4])) {
                    // remove 4th consecutive numbers: 1 before; 1 after.
                    candidate = lot[3] - 1;
                    bag.remove((Object) candidate);
                    candidate = lot[4] + 1;
                    bag.remove((Object) candidate);
                } else if ((lot[0] + 1 == lot[1]) && (lot[2] + 1 == lot[3]) && (lot[3] + 1 == lot[4])) {
                    // remove 4th consecutive numbers: 1 before; 1 after.
                    candidate = lot[0] - 1;
                    bag.remove((Object) candidate);
                    candidate = lot[2] + 1;
                    bag.remove((Object) candidate);
                }
            }

            // Rule: No more than 3 pairs, of which 2 pairs are a triple.
            if (pickIndex >= 3) {
                if ((lot[0] + 1 == lot[1]) && (lot[2] + 1 == lot[3])) {
                    // remove 3rd pair.
                    candidate = lot[4] - 1;
                    bag.remove((Object) candidate);
                    candidate = lot[4] + 1;
                    bag.remove((Object) candidate);
                } else if ((lot[0] + 1 == lot[1]) && (lot[3] + 1 == lot[4])) {
                    // remove 3rd pair.
                    candidate = lot[2] - 1;
                    bag.remove((Object) candidate);
                    candidate = lot[2] + 1;
                    bag.remove((Object) candidate);
                } else if ((lot[1] + 1 == lot[2]) && (lot[3] + 1 == lot[4])) {
                    // remove 3rd pair.
                    candidate = lot[1] - 1;
                    bag.remove((Object) candidate);
                    candidate = lot[1] + 1;
                    bag.remove((Object) candidate);
                }
            }

            if (pickIndex + 1 == SIZE) {
                // Rule: Minimum-valued ball cannot exceed MAX_LOWER.
                candidate = lot[0];
                if (candidate > MAX_LOWER) {
                    throw new LotException("Minimum-valued ball " + candidate + " exceeds " + MAX_LOWER);
                }
                // Rule: Maximum-valued ball cannot precede MIN_UPPER.
                candidate = lot[size - 1];
                if (candidate < MIN_UPPER) {
                    throw new LotException("Maximum-valued ball " + candidate + " less than " + MIN_UPPER);
                }

                // Rule: Biggest gap between 2 balls.
                for (int l = 0, l1 = 1; l1 < size; l++, l1++) {
                    if (lot[l] + MAX_GAP <= lot[l1]) {
                        throw new LotException("Widest gap between " + lot[l] + " and " + lot[l1] + " exceeds " + MAX_GAP);
                    }
                }
            }
        }
    }

}
