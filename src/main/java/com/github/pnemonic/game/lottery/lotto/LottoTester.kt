package com.github.pnemonic.game.lottery.lotto

import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.NumberStatisticGrouping
import com.github.pnemonic.game.Tester
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryResultsReader
import java.io.File
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

/**
 * Test various strategies for "Lotto".
 *
 * @author moshew
 */
class LottoTester : Tester(Lotto()) {
    private val thresholdCandidates: Int = numBalls * THRESHOLD_CANDIDATES_PERCENT / 100
    private val thresholdCandidatesAnd: Int = min(thresholdCandidates * 3 / 2, numBalls / 2)
    private val thresholdCandidatesOr: Int = thresholdCandidates / 2
    private val thresholdCandidatesOr3: Int = thresholdCandidates / 3
    private val thresholdCandidatesOr4: Int = thresholdCandidates / 4
    private val candidates: MutableList<Int> = ArrayList(numBalls)

    /**
     * Creates a new tester.
     */
    init {
        require(thresholdCandidates >= lotterySize) { "too few candidates" }
    }

    @Throws(IOException::class)
    override fun parse(file: File) {
        val reader: LotteryResultsReader = LottoResultsReader()
        val records = reader.parse(file)
        this.records = records
        recordsSize = records.size
        numGamesTotal = recordsSize * Lotto.PLAYS
    }

    override fun drive() {
        val stats = LottoStats(Lotto())
        try {
            stats.processRecords(records)
            numStats = stats.numStats
            for (grouping in NumberStatisticGrouping.values()) {
                drive(grouping, grouping.name)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun drive(grouping: NumberStatisticGrouping, name: String) {
        var games: Set<LotteryGame>
        var score: Int
        var maxScore = 0
        var totScore = 0
        var win = 0
        var recordIndex = 0
        candidates.clear()
        for (record in records) {
            lottery.setCandidates(candidates)
            games = play(Lotto.PLAYS)
            for (game in games) {
                score = record.compareTo(game)
                totScore += score
                maxScore = max(score, maxScore)
            }
            win += if (maxScore == WIN) 1 else 0
            nextCandidates(grouping, recordIndex)
            recordIndex++
        }
        val aveScore = totScore / numGamesTotal
        val winPercent = win.toFloat() / recordsSize * 100
        println("$name:\t{max. $maxScore;\tave. $aveScore;\twin $winPercent%}")
    }

    /**
     * Use statistics to determine the next candidates.
     */
    protected fun nextCandidates(grouping: NumberStatisticGrouping, row: Int) {
        // Get the statistics.
        val nstatRow: Array<NumberStatistic?> = numStats!![row]
        candidates.clear()
        var add: Boolean
        for (nr in nstatRow) {
            val nstat = nr!!
            add = nstat.repeat < MAX_REPEAT_THRESHOLD
            when (grouping) {
                NumberStatisticGrouping.REGULAR -> {}
                NumberStatisticGrouping.LEAST_REPEAT -> add =
                    add and (nstat.indexLeastRepeat < thresholdCandidates)

                NumberStatisticGrouping.MOST_REPEAT -> add =
                    add and (nstat.indexMostRepeat < thresholdCandidates)

                NumberStatisticGrouping.LC -> add =
                    add and (nstat.indexLeastCount < thresholdCandidates)

                NumberStatisticGrouping.LU -> add =
                    add and (nstat.indexLeastUsed < thresholdCandidates)

                NumberStatisticGrouping.LU_LC -> add =
                    add and (nstat.indexLeastUsed < thresholdCandidatesOr || nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.LU_MC -> add =
                    add and (nstat.indexLeastUsed < thresholdCandidatesOr || nstat.indexMostCount < thresholdCandidatesOr)

                NumberStatisticGrouping.LU_MC_LC -> add =
                    add and (nstat.indexLeastUsed < thresholdCandidatesOr3 || nstat.indexMostCount < thresholdCandidatesOr3 || nstat.indexLeastCount < thresholdCandidatesOr3)

                NumberStatisticGrouping.MC -> add =
                    add and (nstat.indexMostCount < thresholdCandidates)

                NumberStatisticGrouping.MC_LC -> add =
                    add and (nstat.indexMostCount < thresholdCandidatesOr || nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MU -> add =
                    add and (nstat.indexMostUsed < thresholdCandidates)

                NumberStatisticGrouping.MU_LC -> add =
                    add and (nstat.indexMostUsed < thresholdCandidatesOr || nstat.indexLeastCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_LU -> add =
                    add and (nstat.indexMostUsed < thresholdCandidatesOr || nstat.indexLeastUsed < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_LU_LC -> add =
                    add and (nstat.indexMostUsed < thresholdCandidatesOr3 || nstat.indexLeastUsed < thresholdCandidatesOr3 || nstat.indexLeastCount < thresholdCandidatesOr3)

                NumberStatisticGrouping.MU_LU_MC -> add =
                    add and (nstat.indexMostUsed < thresholdCandidatesOr3 || nstat.indexLeastUsed < thresholdCandidatesOr3 || nstat.indexMostCount < thresholdCandidatesOr3)

                NumberStatisticGrouping.MU_LU_MC_LC -> add =
                    add and (nstat.indexMostUsed < thresholdCandidatesOr4 || nstat.indexLeastUsed < thresholdCandidatesOr4 || nstat.indexMostCount < thresholdCandidatesOr4 || nstat.indexLeastCount < thresholdCandidatesOr4)

                NumberStatisticGrouping.MU_MC -> add =
                    add and (nstat.indexMostUsed < thresholdCandidatesOr || nstat.indexMostCount < thresholdCandidatesOr)

                NumberStatisticGrouping.MU_MC_LC -> add =
                    add and (nstat.indexMostUsed < thresholdCandidatesOr3 || nstat.indexMostCount < thresholdCandidatesOr3 || nstat.indexLeastCount < thresholdCandidatesOr3)

                NumberStatisticGrouping.LC_AND_LU -> add =
                    add and (nstat.indexLeastCount < thresholdCandidatesAnd && nstat.indexLeastUsed < thresholdCandidatesAnd)

                NumberStatisticGrouping.LC_AND_MU -> add =
                    add and (nstat.indexLeastCount < thresholdCandidatesAnd && nstat.indexMostUsed < thresholdCandidatesAnd)

                NumberStatisticGrouping.MC_AND_LU -> add =
                    add and (nstat.indexMostCount < thresholdCandidatesAnd && nstat.indexLeastUsed < thresholdCandidatesAnd)

                NumberStatisticGrouping.MC_AND_MU -> add =
                    add and (nstat.indexMostCount < thresholdCandidatesAnd && nstat.indexMostUsed < thresholdCandidatesAnd)
            }
            if (add) {
                candidates.add(nstat.id)
            }
        }
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