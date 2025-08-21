package com.github.pnemonic.game.lottery.pais777

import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.lottery.LotteryResultsReader
import com.github.pnemonic.game.lottery.LotteryStats
import java.io.File

/**
 * 777 statistics.
 */
class Lotto777Stats(lottery: Lotto777) : LotteryStats<Lotto777>(lottery) {
    override fun createResultsReader(): LotteryResultsReader {
        return Lotto777ResultsReader()
    }

    override fun printNumberStats() {
        println("max. repeat: $maxRepeat")
        val numPairs = this.numPairs
        if (numPairs != null) {
            var p: StringBuilder
            for (numPairsRow in numPairs) {
                p = StringBuilder()
                p.append('{')
                for (count in numPairsRow) {
                    p.append(count).append(',')
                }
                p.deleteCharAt(p.length - 1)
                p.append('}').append(',')
                println(p)
            }
        }
        val thresholdCandidates = (numBalls * THRESHOLD_CANDIDATES_PERCENT) / 100
        val thresholdCandidatesOr4 = thresholdCandidates / 4
        val numStats = numberStatistics
        val nstatRow: Array<NumberStatistic> = numStats[numStats.lastIndex]
        val asJava = StringBuilder()
        var add: Boolean
        for (nstat in nstatRow) {
            println(nstat)
            add = nstat.id in lotteryMin..lotteryMax
            add = add && (nstat.repeat < maxRepeat)
            // Copy from Tester#nextCandidates and paste here:
            add = add && (nstat.indexMostUsed < thresholdCandidatesOr4 || nstat.indexLeastUsed < thresholdCandidatesOr4 || nstat.indexMostCount < thresholdCandidatesOr4 || nstat.indexLeastCount < thresholdCandidatesOr4)
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
    val fileName = if (args.isEmpty()) "results/777.csv" else args[0]
    val file = File(fileName)
    val stats = Lotto777Stats(Lotto777())
    stats.parse(file)
}