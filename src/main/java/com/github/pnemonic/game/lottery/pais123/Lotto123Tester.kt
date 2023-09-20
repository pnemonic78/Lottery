package com.github.pnemonic.game.lottery.pais123;

import com.github.pnemonic.game.NumberStatistic;
import com.github.pnemonic.game.lottery.Lottery;
import com.github.pnemonic.game.lottery.LotteryGame;
import com.github.pnemonic.game.lottery.LotteryRecord;
import com.github.pnemonic.game.lottery.LotteryResultsReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Test various strategies for "123".
 *
 * @author moshew
 */
public class Lotto123Tester {

    // per hundred
    private static final int THRESHOLD_CANDIDATES_PERCENT = 50;

    private static final int WIN = 300;
    /**
     * Maximum repeat of same number, even though statistically maximum is 9.
     */
    private static final int MAX_REPEAT_THRESHOLD = 8;

    private static enum CompareBy {
        REGULAR, LEAST_COUNT, MOST_COUNT, LEAST_USED, MOST_USED, LEAST_REPEAT, MOST_REPEAT, COUNT_223, COUNT_232, COUNT_322, LEAST_COUNT_LEAST_USED, LEAST_COUNT_MOST_USED, MOST_COUNT_LEAST_USED, MOST_COUNT_MOST_USED
    }

    private List<LotteryRecord> records;
    private int recordsSize;
    private final Lottery lottery;
    private final int lotterySize;
    private final int lotteryMin;
    private final int lotteryMax;
    private final int numBalls;
    private final int thresholdCandidates;
    private final List<Integer> candidates;
    private NumberStatistic[][] numStats;
    private int numGamesTotal;

    public Lotto123Tester() {
        super();
        this.lottery = new Lotto123();
        this.lotterySize = lottery.size();
        this.lotteryMin = lottery.getMinimum();
        this.lotteryMax = lottery.getMaximum();
        this.numBalls = lottery.getNumberBalls();
        this.thresholdCandidates = (numBalls * THRESHOLD_CANDIDATES_PERCENT) / 100;
        this.candidates = new ArrayList<Integer>(numBalls);

        if (thresholdCandidates < lotterySize) {
            throw new IllegalArgumentException("too few candidates");
        }
    }

    /**
     * Main method.
     *
     * @param args the array of arguments.
     */
    public static void main(String[] args) {
        String fileName = (args.length == 0) ? "results/777.csv" : args[0];
        File file = new File(fileName);
        Lotto123Tester tester = new Lotto123Tester();
        try {
            tester.parse(file);
            tester.drive();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parse(File file) throws IOException {
        LotteryResultsReader reader = new Lotto123ResultsReader();
        this.records = reader.parse(file);
        this.recordsSize = records.size();
        this.numGamesTotal = recordsSize * Lotto123.PLAYS;
    }

    public void drive() {
        Lotto123Stats stats = new Lotto123Stats(lottery);
        try {
            stats.processRecords(records, false, true);
            this.numStats = stats.getNumberStatistics();

            driveRegular();
            driveLeastCount();
            driveMostCount();
            driveLeastUsed();
            driveMostUsed();
            // // driveLeastRepeated();
            // // driveMostRepeated();
            driveLeastCountLeastUsed();
            driveLeastCountMostUsed();
            driveMostCountLeastUsed();
            driveMostCountMostUsed();
            // driveCount223();
            // driveCount232();
            // driveCount322();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void driveLeastUsed() {
        drive(CompareBy.LEAST_USED, "least used");
    }

    protected void driveMostUsed() {
        drive(CompareBy.MOST_USED, "most used");
    }

    protected void driveLeastCount() {
        drive(CompareBy.LEAST_COUNT, "least count");
    }

    protected void driveMostCount() {
        drive(CompareBy.MOST_COUNT, "most count");
    }

    protected void driveLeastRepeated() {
        drive(CompareBy.LEAST_REPEAT, "least repeated");
    }

    protected void driveMostRepeated() {
        drive(CompareBy.MOST_REPEAT, "most repeated");
    }

    protected void driveRegular() {
        drive(CompareBy.REGULAR, "regular");
    }

    protected void driveCount223() {
        drive(CompareBy.COUNT_223, "2-2-3");
    }

    protected void driveCount232() {
        drive(CompareBy.COUNT_232, "2-3-2");
    }

    protected void driveCount322() {
        drive(CompareBy.COUNT_322, "3-2-2");
    }

    protected void driveLeastCountLeastUsed() {
        drive(CompareBy.LEAST_COUNT_LEAST_USED, "least count, least used");
    }

    protected void driveLeastCountMostUsed() {
        drive(CompareBy.LEAST_COUNT_MOST_USED, "least count, most used");
    }

    protected void driveMostCountLeastUsed() {
        drive(CompareBy.MOST_COUNT_LEAST_USED, "most count, least used");
    }

    protected void driveMostCountMostUsed() {
        drive(CompareBy.MOST_COUNT_MOST_USED, "most count, most used");
    }

    protected void drive(CompareBy comparator, String name) {
        Set<LotteryGame> games;
        int score;
        int maxScore = 0;
        int totScore = 0;
        int win = 0;
        int recordIndex = 0;

        candidates.clear();

        for (LotteryRecord record : records) {
            lottery.setCandidates(candidates);

            games = play(Lotto123.PLAYS);
            score = 0;
            for (LotteryGame game : games) {
                score = record.compareTo(game);
                totScore += score;
                maxScore = Math.max(score, maxScore);
            }
            win += (maxScore == WIN) ? 1 : 0;

            nextCandidates(comparator, recordIndex);

            recordIndex++;
        }
        int aveScore = totScore / numGamesTotal;
        float winPercent = ((float) win / recordsSize) * 100;
        System.out.println(name + ":\t{max. " + maxScore + ";\tave. " + aveScore + ";\twin " + winPercent + "%}");
    }

    protected Set<LotteryGame> play(int numGames) {
        Set<LotteryGame> games = lottery.play(numGames);
        int gamesSize = games.size();
        while ((gamesSize < numGames) && (gamesSize > 1)) {
            games.addAll(lottery.play(numGames - gamesSize));
            gamesSize = games.size();
        }
        return games;
    }

    /**
     * Use statistics to determine the next candidates.
     */
    protected void nextCandidates(CompareBy comparator, int row) {
        // Get the statistics.
        NumberStatistic[] nstatRow = numStats[row];
        candidates.clear();
        boolean add;

        for (NumberStatistic nstat : nstatRow) {
            add = nstat.repeat < MAX_REPEAT_THRESHOLD;
            switch (comparator) {
                case LEAST_COUNT:
                    add &= nstat.indexLeastCount < thresholdCandidates;
                    break;
                case LEAST_REPEAT:
                    add &= nstat.indexLeastRepeat < thresholdCandidates;
                    break;
                case LEAST_USED:
                    add &= nstat.indexLeastUsed < thresholdCandidates;
                    break;
                case MOST_COUNT:
                    add &= nstat.indexMostCount < thresholdCandidates;
                    break;
                case MOST_REPEAT:
                    add &= nstat.indexMostRepeat < thresholdCandidates;
                    break;
                case MOST_USED:
                    add &= nstat.indexMostUsed < thresholdCandidates;
                    break;
                case LEAST_COUNT_LEAST_USED:
                    add &= nstat.indexLeastCount < thresholdCandidates;
                    add &= nstat.indexLeastUsed < thresholdCandidates;
                    break;
                case LEAST_COUNT_MOST_USED:
                    add &= nstat.indexLeastCount < thresholdCandidates;
                    add &= nstat.indexMostUsed < thresholdCandidates;
                    break;
                case MOST_COUNT_LEAST_USED:
                    add &= nstat.indexMostCount < thresholdCandidates;
                    add &= nstat.indexLeastUsed < thresholdCandidates;
                    break;
                case MOST_COUNT_MOST_USED:
                    add &= nstat.indexMostCount < thresholdCandidates;
                    add &= nstat.indexMostUsed < thresholdCandidates;
                    break;
                default:
                    break;
            }
            if (add) {
                candidates.add(nstat.id);
            }
        }
    }
}
