package com.github.pnemonic.game.lottery.lotto

import com.github.pnemonic.game.NumberStatisticGrouping
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryResultsReader
import com.github.pnemonic.game.lottery.LotteryTester
import com.github.pnemonic.game.lottery.pais777.Lotto777.Companion.COST
import com.github.pnemonic.game.lottery.pais777.Lotto777Tester.Companion.BUDGET
import java.io.File
import kotlin.math.max
import kotlin.math.min

/**
 * Test various strategies for "Lotto".
 */
class LottoTester : LotteryTester<Lotto, LottoStats>(Lotto()) {
    private val thresholdCandidates: Int = (numBalls * THRESHOLD_CANDIDATES_PERCENT) / 100
    private val thresholdCandidatesOr: Int = thresholdCandidates / 2

    init {
        require(thresholdCandidates >= lotterySize) { "too few candidates" }
    }

    override fun createResultsReader(): LotteryResultsReader {
        return LottoResultsReader()
    }

    override fun createStats(lottery: Lotto): LottoStats {
        return LottoStats(lottery)
    }

    override fun drive(grouping: NumberStatisticGrouping, stats: LottoStats) {
        // Total number of plays per budget. Lotto played in pairs.
        val numPlays = (BUDGET / COST) and 1.inv()
        var games: Collection<LotteryGame>
        var score: Int
        var maxScore = 0
        var totalScore = 0
        var win = 0
        var recordIndex = -1
        var numGamesTotal = 0
        for (record in records) {
            predictNextCandidates(grouping, stats, recordIndex)
            games = play(numPlays, record)
            for (game in games) {
                score = record.compareTo(game)
                totalScore += score
                maxScore = max(score, maxScore)
            }
            win += if (maxScore == WIN) 1 else 0
            numGamesTotal += games.size
            recordIndex++
        }
        val aveScore = totalScore / numGamesTotal
        val winPercent = (win * 100f) / records.size
        println("$grouping:\t{max. $maxScore;\tave. $aveScore;\twin $winPercent%}")
    }

    /**
     * Use statistics to determine the next candidates.
     */
    private fun predictNextCandidates(grouping: NumberStatisticGrouping, stats: LottoStats, row: Int) {
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
                    add = add && (nstat.indexMostUsed < thresholdCandidatesOr ||
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
        private const val WIN = 601

        /**
         * Maximum repeat of same number.
         */
        private const val MAX_REPEAT_THRESHOLD = 8
    }
}

/**
 * Main method.
 *
 * @param args the array of arguments.
 */
fun main(args: Array<String>) {
    val fileName = if (args.isEmpty()) "results/Lotto.csv" else args[0]
    val file = File(fileName)
    val tester = LottoTester()
    try {
        tester.parse(file)
        tester.drive()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}