package com.github.pnemonic.game.lottery.lotto;

import com.github.pnemonic.game.NumberStatistic;
import com.github.pnemonic.game.NumberStatisticGrouping;
import com.github.pnemonic.game.Tester;
import com.github.pnemonic.game.lottery.LotteryGame;
import com.github.pnemonic.game.lottery.LotteryRecord;
import com.github.pnemonic.game.lottery.LotteryResultsReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Test various strategies for "Lotto".
 *
 * @author moshew
 */
public class LottoTester extends Tester {

    // per hundred
    private static final int THRESHOLD_CANDIDATES_PERCENT = 50;

    private static final int WIN = 601;
    /**
     * Maximum repeat of same number.
     */
    private static final int MAX_REPEAT_THRESHOLD = 8;

    private final int thresholdCandidates;
    private final int thresholdCandidatesAnd;
    private final int thresholdCandidatesOr;
    private final int thresholdCandidatesOr3;
    private final int thresholdCandidatesOr4;
    private final List<Integer> candidates;

    /**
     * Creates a new tester.
     */
    public LottoTester() {
        super(new Lotto());
        thresholdCandidates = (numBalls * THRESHOLD_CANDIDATES_PERCENT) / 100;
        thresholdCandidatesAnd = Math.min((thresholdCandidates * 3) / 2, numBalls / 2);
        thresholdCandidatesOr = thresholdCandidates / 2;
        thresholdCandidatesOr3 = thresholdCandidates / 3;
        thresholdCandidatesOr4 = thresholdCandidates / 4;
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
        String fileName = (args.length == 0) ? "results/Lotto.csv" : args[0];
        File file = new File(fileName);
        LottoTester tester = new LottoTester();
        try {
            tester.parse(file);
            tester.drive();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parse(File file) throws IOException {
        LotteryResultsReader reader = new LottoResultsReader();
        this.records = reader.parse(file);
        this.recordsSize = records.size();
        this.numGamesTotal = recordsSize * Lotto.PLAYS;
    }

    @Override
    public void drive() {
        LottoStats stats = new LottoStats(new Lotto());
        try {
            stats.processRecords(records);
            this.numStats = stats.getNumberStatistics();

            for (NumberStatisticGrouping grouping : NumberStatisticGrouping.values()) {
                drive(grouping, grouping.name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void drive(NumberStatisticGrouping grouping, String name) {
        Set<LotteryGame> games;
        int score;
        int maxScore = 0;
        int totScore = 0;
        int win = 0;
        int recordIndex = 0;

        candidates.clear();

        for (LotteryRecord record : records) {
            lottery.setCandidates(candidates);

            games = play(Lotto.PLAYS);
            score = 0;
            for (LotteryGame game : games) {
                score = record.compareTo(game);
                totScore += score;
                maxScore = Math.max(score, maxScore);
            }
            win += (maxScore == WIN) ? 1 : 0;

            nextCandidates(grouping, recordIndex);

            recordIndex++;
        }
        int aveScore = totScore / numGamesTotal;
        float winPercent = ((float) win / recordsSize) * 100;
        System.out.println(name + ":\t{max. " + maxScore + ";\tave. " + aveScore + ";\twin " + winPercent + "%}");
    }

    /**
     * Use statistics to determine the next candidates.
     */
    protected void nextCandidates(NumberStatisticGrouping grouping, int row) {
        // Get the statistics.
        NumberStatistic[] nstatRow = numStats[row];
        candidates.clear();
        boolean add;

        for (NumberStatistic nstat : nstatRow) {
            add = nstat.repeat < MAX_REPEAT_THRESHOLD;
            switch (grouping) {
                case REGULAR:
                    break;
                case LEAST_REPEAT:
                    add &= (nstat.indexLeastRepeat < thresholdCandidates);
                    break;
                case MOST_REPEAT:
                    add &= (nstat.indexMostRepeat < thresholdCandidates);
                    break;
                case LC:
                    add &= (nstat.indexLeastCount < thresholdCandidates);
                    break;
                case LU:
                    add &= (nstat.indexLeastUsed < thresholdCandidates);
                    break;
                case LU_LC:
                    add &= (nstat.indexLeastUsed < thresholdCandidatesOr) || (nstat.indexLeastCount < thresholdCandidatesOr);
                    break;
                case LU_MC:
                    add &= (nstat.indexLeastUsed < thresholdCandidatesOr) || (nstat.indexMostCount < thresholdCandidatesOr);
                    break;
                case LU_MC_LC:
                    add &= (nstat.indexLeastUsed < thresholdCandidatesOr3) || (nstat.indexMostCount < thresholdCandidatesOr3)
                        || (nstat.indexLeastCount < thresholdCandidatesOr3);
                    break;
                case MC:
                    add &= (nstat.indexMostCount < thresholdCandidates);
                    break;
                case MC_LC:
                    add &= (nstat.indexMostCount < thresholdCandidatesOr) || (nstat.indexLeastCount < thresholdCandidatesOr);
                    break;
                case MU:
                    add &= (nstat.indexMostUsed < thresholdCandidates);
                    break;
                case MU_LC:
                    add &= (nstat.indexMostUsed < thresholdCandidatesOr) || (nstat.indexLeastCount < thresholdCandidatesOr);
                    break;
                case MU_LU:
                    add &= (nstat.indexMostUsed < thresholdCandidatesOr) || (nstat.indexLeastUsed < thresholdCandidatesOr);
                    break;
                case MU_LU_LC:
                    add &= (nstat.indexMostUsed < thresholdCandidatesOr3) || (nstat.indexLeastUsed < thresholdCandidatesOr3)
                        || (nstat.indexLeastCount < thresholdCandidatesOr3);
                    break;
                case MU_LU_MC:
                    add &= (nstat.indexMostUsed < thresholdCandidatesOr3) || (nstat.indexLeastUsed < thresholdCandidatesOr3)
                        || (nstat.indexMostCount < thresholdCandidatesOr3);
                    break;
                case MU_LU_MC_LC:
                    add &= (nstat.indexMostUsed < thresholdCandidatesOr4) || (nstat.indexLeastUsed < thresholdCandidatesOr4)
                        || (nstat.indexMostCount < thresholdCandidatesOr4) || (nstat.indexLeastCount < thresholdCandidatesOr4);
                    break;
                case MU_MC:
                    add &= (nstat.indexMostUsed < thresholdCandidatesOr) || (nstat.indexMostCount < thresholdCandidatesOr);
                    break;
                case MU_MC_LC:
                    add &= (nstat.indexMostUsed < thresholdCandidatesOr3) || (nstat.indexMostCount < thresholdCandidatesOr3)
                        || (nstat.indexLeastCount < thresholdCandidatesOr3);
                    break;
                case LC_AND_LU:
                    add &= (nstat.indexLeastCount < thresholdCandidatesAnd) && (nstat.indexLeastUsed < thresholdCandidatesAnd);
                    break;
                case LC_AND_MU:
                    add &= (nstat.indexLeastCount < thresholdCandidatesAnd) && (nstat.indexMostUsed < thresholdCandidatesAnd);
                    break;
                case MC_AND_LU:
                    add &= (nstat.indexMostCount < thresholdCandidatesAnd) && (nstat.indexLeastUsed < thresholdCandidatesAnd);
                    break;
                case MC_AND_MU:
                    add &= (nstat.indexMostCount < thresholdCandidatesAnd) && (nstat.indexMostUsed < thresholdCandidatesAnd);
                    break;
                default:
                    System.err.println("unhandled type " + grouping);
                    break;
            }
            if (add) {
                candidates.add(nstat.id);
            }
        }
    }
}
