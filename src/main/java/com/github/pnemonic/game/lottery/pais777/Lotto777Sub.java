package com.github.pnemonic.game.lottery.pais777;

import com.github.pnemonic.game.lottery.LotException;
import com.github.pnemonic.game.lottery.Lottery;
import com.github.pnemonic.game.lottery.LotteryGame;

import java.util.Set;

/**
 * Choose numbers for 777 for Israel - balls 1 to 35.
 *
 * @author Moshe
 */
public class Lotto777Sub extends Lotto777 {

    private static final int MAX_LOWER = 23;
    private static final int MIN_UPPER = 12;
    private static final int MAX_GAP = 28;
    private static final int MIN_ODD = 2;
    private static final int MIN_EVEN = 2;

    private static final int MINIMUM = 1;

    private static final int[][] PAIRS_COUNT = {
        {0, 304, 220, 161, 154, 109, 70, 70, 52, 36, 23, 21, 11, 6, 5, 7, 1, 1, 2, 2, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {304, 0, 281, 246, 191, 129, 90, 86, 53, 53, 31, 28, 20, 12, 8, 3, 8, 4, 1, 1, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {220, 281, 0, 272, 216, 174, 144, 94, 70, 56, 50, 45, 29, 16, 19, 14, 7, 8, 4, 5, 2, 4, 1, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {161, 246, 272, 0, 263, 225, 146, 141, 101, 71, 64, 46, 36, 22, 20, 9, 13, 10, 5, 2, 2, 1, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0},
        {154, 191, 216, 263, 0, 275, 212, 191, 123, 103, 63, 66, 54, 26, 35, 22, 19, 9, 7, 4, 4, 4, 1, 3, 2, 1, 1, 1, 0, 1, 0, 0, 0, 0, 0},
        {109, 129, 174, 225, 275, 0, 289, 222, 178, 139, 90, 65, 42, 50, 33, 22, 33, 8, 10, 9, 3, 2, 3, 3, 3, 0, 0, 2, 0, 0, 0, 0, 0, 1, 0},
        {70, 90, 144, 146, 212, 289, 0, 295, 216, 167, 128, 100, 63, 54, 44, 35, 30, 13, 11, 14, 4, 6, 6, 3, 0, 0, 1, 0, 0, 2, 2, 0, 0, 0, 0},
        {70, 86, 94, 141, 191, 222, 295, 0, 296, 239, 172, 132, 106, 84, 77, 56, 29, 19, 17, 11, 12, 4, 6, 4, 3, 0, 2, 0, 0, 1, 0, 0, 0, 0, 0},
        {52, 53, 70, 101, 123, 178, 216, 296, 0, 297, 217, 173, 130, 104, 78, 65, 36, 33, 26, 13, 13, 12, 4, 7, 3, 2, 4, 1, 2, 2, 0, 0, 0, 0, 0},
        {36, 53, 56, 71, 103, 139, 167, 239, 297, 0, 281, 240, 180, 150, 112, 71, 61, 43, 31, 27, 20, 14, 12, 8, 3, 7, 3, 1, 1, 0, 0, 0, 0, 1, 0},
        {23, 31, 50, 64, 63, 90, 128, 172, 217, 281, 0, 284, 218, 175, 134, 100, 69, 51, 38, 39, 35, 20, 13, 10, 10, 4, 4, 0, 0, 2, 0, 1, 0, 0, 0},
        {21, 28, 45, 46, 66, 65, 100, 132, 173, 240, 284, 0, 290, 219, 170, 131, 108, 94, 53, 58, 31, 32, 22, 14, 14, 7, 12, 5, 1, 4, 1, 0, 1, 0, 0},
        {11, 20, 29, 36, 54, 42, 63, 106, 130, 180, 218, 290, 0, 294, 222, 185, 125, 97, 70, 60, 29, 36, 25, 15, 14, 18, 6, 6, 3, 4, 1, 0, 1, 2, 0},
        {6, 12, 16, 22, 26, 50, 54, 84, 104, 150, 175, 219, 294, 0, 321, 195, 193, 116, 93, 82, 62, 43, 34, 26, 20, 15, 9, 9, 2, 4, 5, 3, 1, 1, 1},
        {5, 8, 19, 20, 35, 33, 44, 77, 78, 112, 134, 170, 222, 321, 0, 326, 247, 161, 147, 91, 68, 73, 43, 35, 27, 23, 19, 11, 5, 6, 4, 3, 5, 1, 1},
        {7, 3, 14, 9, 22, 22, 35, 56, 65, 71, 100, 131, 185, 195, 326, 0, 304, 202, 164, 143, 106, 86, 57, 52, 40, 24, 22, 15, 10, 10, 7, 2, 4, 3, 3},
        {1, 8, 7, 13, 19, 33, 30, 29, 36, 61, 69, 108, 125, 193, 247, 304, 0, 262, 265, 173, 154, 105, 91, 60, 44, 40, 22, 23, 13, 12, 11, 6, 5, 2, 4},
        {1, 4, 8, 10, 9, 8, 13, 19, 33, 43, 51, 94, 97, 116, 161, 202, 262, 0, 264, 212, 132, 134, 100, 85, 60, 38, 39, 22, 12, 8, 9, 4, 3, 4, 2},
        {2, 1, 4, 5, 7, 10, 11, 17, 26, 31, 38, 53, 70, 93, 147, 164, 265, 264, 0, 314, 194, 173, 112, 112, 70, 57, 42, 37, 27, 23, 11, 12, 9, 7, 4},
        {2, 1, 5, 2, 4, 9, 14, 11, 13, 27, 39, 58, 60, 82, 91, 143, 173, 212, 314, 0, 298, 210, 178, 157, 92, 89, 55, 45, 43, 24, 20, 17, 11, 10, 3},
        {1, 0, 2, 2, 4, 3, 4, 12, 13, 20, 35, 31, 29, 62, 68, 106, 154, 132, 194, 298, 0, 258, 230, 187, 114, 88, 62, 62, 35, 34, 30, 17, 15, 7, 7},
        {0, 2, 4, 1, 4, 2, 6, 4, 12, 14, 20, 32, 36, 43, 73, 86, 105, 134, 173, 210, 258, 0, 263, 222, 183, 157, 101, 76, 65, 39, 39, 23, 14, 14, 7},
        {0, 2, 1, 1, 1, 3, 6, 6, 4, 12, 13, 22, 25, 34, 43, 57, 91, 100, 112, 178, 230, 263, 0, 282, 222, 164, 138, 110, 87, 46, 38, 35, 26, 15, 15},
        {0, 0, 0, 0, 3, 3, 3, 4, 7, 8, 10, 14, 15, 26, 35, 52, 60, 85, 112, 157, 187, 222, 282, 0, 290, 229, 189, 136, 115, 85, 52, 58, 24, 20, 18},
        {0, 0, 4, 0, 2, 3, 0, 3, 3, 3, 10, 14, 14, 20, 27, 40, 44, 60, 70, 92, 114, 183, 222, 290, 0, 278, 201, 167, 125, 97, 83, 63, 48, 42, 31},
        {0, 0, 0, 0, 1, 0, 0, 0, 2, 7, 4, 7, 18, 15, 23, 24, 40, 38, 57, 89, 88, 157, 164, 229, 278, 0, 281, 237, 169, 132, 127, 86, 58, 44, 30},
        {0, 0, 0, 2, 1, 0, 1, 2, 4, 3, 4, 12, 6, 9, 19, 22, 22, 39, 42, 55, 62, 101, 138, 189, 201, 281, 0, 296, 219, 144, 149, 92, 81, 53, 48},
        {1, 0, 0, 0, 1, 2, 0, 0, 1, 1, 0, 5, 6, 9, 11, 15, 23, 22, 37, 45, 62, 76, 110, 136, 167, 237, 296, 0, 270, 227, 175, 142, 98, 94, 50},
        {0, 0, 0, 0, 0, 0, 0, 0, 2, 1, 0, 1, 3, 2, 5, 10, 13, 12, 27, 43, 35, 65, 87, 115, 125, 169, 219, 270, 0, 298, 216, 168, 127, 111, 70},
        {0, 0, 0, 0, 1, 0, 2, 1, 2, 0, 2, 4, 4, 4, 6, 10, 12, 8, 23, 24, 34, 39, 46, 85, 97, 132, 144, 227, 298, 0, 268, 230, 167, 129, 116},
        {0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1, 1, 5, 4, 7, 11, 9, 11, 20, 30, 39, 38, 52, 83, 127, 149, 175, 216, 268, 0, 289, 219, 183, 125},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 3, 3, 2, 6, 4, 12, 17, 17, 23, 35, 58, 63, 86, 92, 142, 168, 230, 289, 0, 287, 254, 177},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 5, 4, 5, 3, 9, 11, 15, 14, 26, 24, 48, 58, 81, 98, 127, 167, 219, 287, 0, 271, 230},
        {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 2, 1, 1, 3, 2, 4, 7, 10, 7, 14, 15, 20, 42, 44, 53, 94, 111, 129, 183, 254, 271, 0, 268},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 3, 4, 2, 4, 3, 7, 7, 15, 18, 31, 30, 48, 50, 70, 116, 125, 177, 230, 268, 0}};

    /**
     * Constructs a new 777.
     */
    public Lotto777Sub() {
        super();
    }

    @Override
    public int getMinimum() {
        return MINIMUM;
    }

    @Override
    public int getMaximum() {
        return 35;
    }

    @Override
    protected void filterGame(LotteryGame game) throws LotException {
        int candidate;
        int[] lot = game.lot;
        int size = lot.length;

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

        // Rule: Minimum number of odd-numbered balls.
        int countOdd = 0;
        int countEven = 0;
        for (int l : lot) {
            if ((l & 1) == 0) {
                countEven++;
            } else {
                countOdd++;
            }
        }
        if (countOdd < MIN_ODD) {
            throw new LotException("Minimum odds less than " + MIN_ODD);
        }
        if (countEven < MIN_EVEN) {
            throw new LotException("Minimum evens less than " + MIN_EVEN);
        }

        // Check that pairs are valid.
        int ball0, ball1;
        int pairIndex0, pairIndex1;
        int pairCount;
        for (int l = 0, l1 = 1; l1 < size; l++, l1++) {
            ball0 = lot[l];
            ball1 = lot[l1];
            pairIndex0 = ball0 - MINIMUM;
            pairIndex1 = ball1 - MINIMUM;
            pairCount = PAIRS_COUNT[pairIndex0][pairIndex1];
            if (pairCount == 0) {
                throw new LotException("Pair is uncommon");
            }
        }
    }

    /**
     * Main method.
     *
     * @param args the array of arguments.
     */
    public static void main(String[] args) {
        Lottery lottery = new Lotto777Sub();
        if (args.length > 0) {
            lottery.setCandidates(args[0]);
        }

        int plays = PLAYS;
        Set<LotteryGame> games = lottery.play(plays);
        for (LotteryGame game : games) {
            lottery.print(game);
        }
    }
}
