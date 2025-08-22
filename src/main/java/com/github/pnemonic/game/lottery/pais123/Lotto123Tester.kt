package com.github.pnemonic.game.lottery.pais123

import com.github.pnemonic.game.NumberStatisticGrouping
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryResultsReader
import com.github.pnemonic.game.lottery.LotteryStats
import com.github.pnemonic.game.lottery.LotteryTester
import com.github.pnemonic.game.lottery.pais123.Lotto123.Companion.COST
import java.io.File

/**
 * Test various strategies for "123".
 */
class Lotto123Tester : LotteryTester<Lotto123>(Lotto123()) {
    private val thresholdCandidates: Int = (numBalls * THRESHOLD_CANDIDATES_PERCENT) / 100
    private val thresholdCandidatesOr: Int = thresholdCandidates / 2

    init {
        require(thresholdCandidates >= lotterySize) { "too few candidates" }
    }

    override fun createResultsReader(): LotteryResultsReader {
        return Lotto123ResultsReader()
    }

    override fun createStats(lottery: Lotto123): LotteryStats<Lotto123> {
        return Lotto123Stats(lottery)
    }

    override fun drive(grouping: NumberStatisticGrouping, stats: LotteryStats<Lotto123>) {
        val numPlays = BUDGET / COST
        var wallet = BUDGET.toLong()
        var games: Collection<LotteryGame>
        var recordIndex = -1
        var numPlaysTotal = 0
        var wins = 0

        for (record in records) {
            nextCandidates(grouping, stats, recordIndex)
            games = play(numPlays, record)
            wallet -= games.size * COST
            for (game in games) {
                wallet += game.prize
                if (game.prize > 0) wins++
            }
            numPlaysTotal += games.size
            recordIndex++
        }
        val aveScore = wallet.toFloat() / numPlaysTotal
        println("$grouping:\t{wallet: $wallet, wins: $wins, ave.: $aveScore}")
    }

    /**
     * Use statistics to determine the next candidates.
     */
    private fun nextCandidates(
        grouping: NumberStatisticGrouping,
        stats: LotteryStats<Lotto123>,
        row: Int
    ) {
        if (row < 0) {
            lottery.setCandidates(null)
            return
        }
        // Get the statistics.
        val numStats = stats.numberStatistics
        val nstatRow = numStats[row]
        val candidates = mutableListOf<Int>()
        var add: Boolean
        for (nstat in nstatRow) {
            add = (nstat.repeat < MAX_REPEAT_THRESHOLD)
            when (grouping) {
                NumberStatisticGrouping.LEAST_COUNT ->
                    add = add && (nstat.indexLeastCount < thresholdCandidates)

                NumberStatisticGrouping.LEAST_REPEAT ->
                    add = add && (nstat.indexLeastRepeat < thresholdCandidates)

                NumberStatisticGrouping.LEAST_USED ->
                    add = add && (nstat.indexLeastUsed < thresholdCandidates)

                NumberStatisticGrouping.MOST_COUNT ->
                    add = add && (nstat.indexMostCount < thresholdCandidates)

                NumberStatisticGrouping.MOST_REPEAT ->
                    add = add && (nstat.indexMostRepeat < thresholdCandidates)

                NumberStatisticGrouping.MOST_USED ->
                    add = add && (nstat.indexMostUsed < thresholdCandidates)

                NumberStatisticGrouping.LC_AND_LU -> {
                    add = add && (nstat.indexLeastCount < thresholdCandidates)
                    add = add && (nstat.indexLeastUsed < thresholdCandidates)
                }

                NumberStatisticGrouping.LC_AND_MU -> {
                    add = add && (nstat.indexLeastCount < thresholdCandidates)
                    add = add && (nstat.indexMostUsed < thresholdCandidates)
                }

                NumberStatisticGrouping.MC_AND_LU -> {
                    add = add && (nstat.indexMostCount < thresholdCandidates)
                    add = add && (nstat.indexLeastUsed < thresholdCandidates)
                }

                NumberStatisticGrouping.MC_AND_MU -> {
                    add = add && (nstat.indexMostCount < thresholdCandidates)
                    add = add && (nstat.indexMostUsed < thresholdCandidates)
                }

                NumberStatisticGrouping.LU_LC ->
                    add = add && (nstat.indexLeastUsed < thresholdCandidatesOr ||
                            nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.LU_MC ->
                    add = add && (nstat.indexLeastUsed < thresholdCandidatesOr ||
                            nstat.indexMostCount < thresholdCandidatesOr)

                NumberStatisticGrouping.LU_MC_LC ->
                    add = add && (nstat.indexLeastUsed < thresholdCandidatesOr ||
                            nstat.indexMostCount < thresholdCandidatesOr ||
                            nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MC_LC ->
                    add = add && (nstat.indexMostCount < thresholdCandidatesOr ||
                            nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_LC ->
                    add = add && (nstat.indexMostUsed < thresholdCandidatesOr ||
                            nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_LU ->
                    add = add && (nstat.indexMostUsed < thresholdCandidatesOr ||
                            nstat.indexLeastUsed < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_LU_LC ->
                    add = add && (nstat.indexMostUsed < thresholdCandidatesOr ||
                            nstat.indexLeastUsed < thresholdCandidatesOr ||
                            nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_LU_MC ->
                    add =
                        add && (nstat.indexMostUsed < thresholdCandidatesOr ||
                                nstat.indexLeastUsed < thresholdCandidatesOr ||
                                nstat.indexMostCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_LU_MC_LC ->
                    add = add && (nstat.indexMostUsed < thresholdCandidatesOr ||
                            nstat.indexLeastUsed < thresholdCandidatesOr ||
                            nstat.indexMostCount < thresholdCandidatesOr ||
                            nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_MC ->
                    add = add && (nstat.indexMostUsed < thresholdCandidatesOr ||
                            nstat.indexMostCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_MC_LC ->
                    add = add && (nstat.indexMostUsed < thresholdCandidatesOr ||
                            nstat.indexMostCount < thresholdCandidatesOr ||
                            nstat.indexLeastCount < thresholdCandidatesOr)

                else -> Unit
            }
            if (add) {
                candidates.add(nstat.id)
            }
        }
        lottery.setCandidates(candidates)
    }

    companion object {
        // per hundred
        private const val THRESHOLD_CANDIDATES_PERCENT = 50

        /**
         * Maximum repeat of same number.
         */
        private const val MAX_REPEAT_THRESHOLD = 8

        /**
         * Budget.
         */
        internal const val BUDGET = 20
    }
}

/**
 * Main method.
 *
 * @param args the array of arguments.
 */
fun main(args: Array<String>) {
    val fileName = if (args.isEmpty()) "results/123.csv" else args[0]
    val file = File(fileName)
    val tester = Lotto123Tester()
    try {
        tester.parse(file)
        tester.drive()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}