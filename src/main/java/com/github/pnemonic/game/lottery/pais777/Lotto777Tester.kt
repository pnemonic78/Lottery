package com.github.pnemonic.game.lottery.pais777

import com.github.pnemonic.game.NumberStatisticGrouping
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryResultsReader
import com.github.pnemonic.game.lottery.LotteryStats
import com.github.pnemonic.game.lottery.LotteryTester
import com.github.pnemonic.game.lottery.pais777.Lotto777.Companion.COST
import java.io.File
import kotlin.math.min

/**
 * Test various strategies for "777".
 */
class Lotto777Tester : LotteryTester<Lotto777>(Lotto777()) {
    private val thresholdCandidates: Int = (numBalls * THRESHOLD_CANDIDATES_PERCENT) / 100
    private val thresholdCandidatesAnd: Int = min((thresholdCandidates * 3) / 2, numBalls / 2)
    private val thresholdCandidatesOr: Int = thresholdCandidates / 2
    private val thresholdCandidatesOr3: Int = thresholdCandidates / 3
    private val thresholdCandidatesOr4: Int = thresholdCandidates / 4

    init {
        require(thresholdCandidates >= lotterySize) { "too few candidates" }
    }

    override fun createResultsReader(): LotteryResultsReader {
        return Lotto777ResultsReader()
    }

    override fun createStats(lottery: Lotto777): LotteryStats<Lotto777> {
        return Lotto777Stats(lottery)
    }

    override fun drive(grouping: NumberStatisticGrouping, stats: LotteryStats<Lotto777>) {
        val numPlays = BUDGET / COST
        var wallet = BUDGET.toLong()
        var games: Collection<LotteryGame>
        var recordIndex = -1
        var numPlaysTotal = 0
        var wins = 0

        for (record in records) {
            nextCandidates(grouping, stats, recordIndex)
            wallet -= numPlays * COST
            games = play(numPlays, record)
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
    private fun nextCandidates(grouping: NumberStatisticGrouping, stats: LotteryStats<Lotto777>, row: Int) {
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
                NumberStatisticGrouping.LEAST_REPEAT ->
                    add = add && (nstat.indexLeastRepeat < thresholdCandidates)

                NumberStatisticGrouping.MOST_REPEAT ->
                    add = add && (nstat.indexMostRepeat < thresholdCandidates)

                NumberStatisticGrouping.LEAST_COUNT ->
                    add = add && (nstat.indexLeastCount < thresholdCandidates)

                NumberStatisticGrouping.LEAST_USED ->
                    add = add && (nstat.indexLeastUsed < thresholdCandidates)

                NumberStatisticGrouping.LU_LC ->
                    add =
                        add && (nstat.indexLeastUsed < thresholdCandidatesOr || nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.LU_MC ->
                    add =
                        add && (nstat.indexLeastUsed < thresholdCandidatesOr || nstat.indexMostCount < thresholdCandidatesOr)

                NumberStatisticGrouping.LU_MC_LC ->
                    add =
                        add && (nstat.indexLeastUsed < thresholdCandidatesOr3 || nstat.indexMostCount < thresholdCandidatesOr3 || nstat.indexLeastCount < thresholdCandidatesOr3)

                NumberStatisticGrouping.MOST_COUNT ->
                    add = add && (nstat.indexMostCount < thresholdCandidates)

                NumberStatisticGrouping.MC_LC ->
                    add =
                        add && (nstat.indexMostCount < thresholdCandidatesOr || nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MOST_USED ->
                    add = add && (nstat.indexMostUsed < thresholdCandidates)

                NumberStatisticGrouping.MU_LC ->
                    add =
                        add && (nstat.indexMostUsed < thresholdCandidatesOr || nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_LU ->
                    add =
                        add && (nstat.indexMostUsed < thresholdCandidatesOr || nstat.indexLeastUsed < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_LU_LC ->
                    add =
                        add && (nstat.indexMostUsed < thresholdCandidatesOr3 || nstat.indexLeastUsed < thresholdCandidatesOr3 || nstat.indexLeastCount < thresholdCandidatesOr3)

                NumberStatisticGrouping.MU_LU_MC ->
                    add =
                        add && (nstat.indexMostUsed < thresholdCandidatesOr3 || nstat.indexLeastUsed < thresholdCandidatesOr3 || nstat.indexMostCount < thresholdCandidatesOr3)

                NumberStatisticGrouping.MU_LU_MC_LC ->
                    add =
                        add && (nstat.indexMostUsed < thresholdCandidatesOr4 || nstat.indexLeastUsed < thresholdCandidatesOr4 || nstat.indexMostCount < thresholdCandidatesOr4 || nstat.indexLeastCount < thresholdCandidatesOr4)

                NumberStatisticGrouping.MU_MC ->
                    add =
                        add && (nstat.indexMostUsed < thresholdCandidatesOr || nstat.indexMostCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_MC_LC ->
                    add =
                        add && (nstat.indexMostUsed < thresholdCandidatesOr3 || nstat.indexMostCount < thresholdCandidatesOr3 || nstat.indexLeastCount < thresholdCandidatesOr3)

                NumberStatisticGrouping.LC_AND_LU ->
                    add =
                        add && (nstat.indexLeastCount < thresholdCandidatesAnd && nstat.indexLeastUsed < thresholdCandidatesAnd)

                NumberStatisticGrouping.LC_AND_MU ->
                    add =
                        add && (nstat.indexLeastCount < thresholdCandidatesAnd && nstat.indexMostUsed < thresholdCandidatesAnd)

                NumberStatisticGrouping.MC_AND_LU ->
                    add =
                        add && (nstat.indexMostCount < thresholdCandidatesAnd && nstat.indexLeastUsed < thresholdCandidatesAnd)

                NumberStatisticGrouping.MC_AND_MU ->
                    add =
                        add && (nstat.indexMostCount < thresholdCandidatesAnd && nstat.indexMostUsed < thresholdCandidatesAnd)

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
        internal const val BUDGET = 200
    }
}

/**
 * Main method.
 *
 * @param args the array of arguments.
 */
fun main(args: Array<String>) {
    val fileName = if (args.isEmpty()) "results/777.csv" else args[0]
    val file = File(fileName)
    val tester = Lotto777Tester()
    try {
        tester.parse(file)
        tester.drive()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}