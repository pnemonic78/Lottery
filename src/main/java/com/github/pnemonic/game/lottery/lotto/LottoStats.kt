package com.github.pnemonic.game.lottery.lotto

import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.lottery.LotteryResultsReader
import com.github.pnemonic.game.lottery.LotteryStats
import java.io.File
import kotlin.math.min

/**
 * Lotto statistics.
 */
class LottoStats(lottery: Lotto) : LotteryStats<Lotto>(lottery) {
    override fun createResultsReader(): LotteryResultsReader {
        return LottoResultsReader()
    }

    override fun printNumberStats() {
        println("max. repeat: $maxRepeat")
        val thresholdCandidates = (numBalls * THRESHOLD_CANDIDATES_PERCENT) / 100
        val thresholdCandidatesAnd = min((thresholdCandidates * 3) / 2, numBalls / 2)
        val numStats = numberStatistics
        val nstatRow: Array<NumberStatistic> = numStats[numStats.lastIndex]
        val asJava = StringBuilder()
        var add: Boolean
        for (nstat in nstatRow) {
            println(nstat)
            add = nstat.id >= lotteryMin && nstat.id <= lotteryMax
            add = add && (nstat.repeat < maxRepeat)
            // Copy from Tester#nextCandidates and paste here:
            add =
                add && (nstat.indexLeastCount < thresholdCandidatesAnd && nstat.indexMostUsed < thresholdCandidatesAnd)
            if (add) {
                if (asJava.isNotEmpty()) {
                    asJava.append(',')
                }
                asJava.append(nstat.id)
            }
        }
        println(asJava)
    }

    companion object {
        private const val THRESHOLD_CANDIDATES_PERCENT = 50
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
    val stats = LottoStats(Lotto())
    stats.parse(file)
}