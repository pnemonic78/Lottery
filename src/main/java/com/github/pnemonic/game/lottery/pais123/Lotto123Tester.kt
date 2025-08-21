package com.github.pnemonic.game.lottery.pais123

import com.github.pnemonic.game.NumberStatisticGrouping
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryResultsReader
import com.github.pnemonic.game.lottery.LotteryTester
import com.github.pnemonic.game.lottery.pais123.Lotto123.Companion.COST
import java.io.File

/**
 * Test various strategies for "123".
 */
class Lotto123Tester : LotteryTester(Lotto123()) {
    private val thresholdCandidates: Int = (numBalls * THRESHOLD_CANDIDATES_PERCENT) / 100
    private val candidates: MutableList<Int> = ArrayList(numBalls)

    init {
        require(thresholdCandidates >= lotterySize) { "too few candidates" }
    }

    override fun createResultsReader(): LotteryResultsReader {
        return Lotto123ResultsReader()
    }

    override fun drive() {
        val stats = Lotto123Stats(lottery)
        stats.processRecords(records, false, true)
        numStats = stats.numberStatistics
        for (grouping in NumberStatisticGrouping.entries) {
            drive(grouping)
        }
    }

    override fun drive(grouping: NumberStatisticGrouping) {
        val numPlays = BUDGET / COST
        var games: Collection<LotteryGame>
        var wallet: Long = BUDGET.toLong()
        var recordIndex = 0
        var numGamesTotal = 0
        candidates.clear()
        for (record in records) {
            lottery.setCandidates(candidates)
            wallet -= numPlays * COST
            games = play(numPlays, record)
            for (game in games) {
                wallet += game.prize
            }
            nextCandidates(grouping, recordIndex)
            recordIndex++
            numGamesTotal += games.size
        }
        val aveScore = wallet.toFloat() / numGamesTotal
        println("$grouping:\t{wallet: $wallet, ave.: $aveScore}")
    }

    /**
     * Use statistics to determine the next candidates.
     */
    private fun nextCandidates(grouping: NumberStatisticGrouping, row: Int) {
        // Get the statistics.
        val nstatRow = numStats[row]
        candidates.clear()
        var add: Boolean
        for (nr in nstatRow) {
            val nstat = nr!!
            add = nstat.repeat < MAX_REPEAT_THRESHOLD
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

                else -> Unit
            }
            if (add) {
                candidates.add(nstat.id)
            }
        }
    }

    companion object {
        // per hundred
        private const val THRESHOLD_CANDIDATES_PERCENT = 50

        /**
         * Maximum repeat of same number, even though statistically maximum is 9.
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