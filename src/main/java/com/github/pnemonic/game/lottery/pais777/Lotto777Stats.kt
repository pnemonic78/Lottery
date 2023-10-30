package com.github.pnemonic.game.lottery.pais777

import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryResultsReader
import com.github.pnemonic.game.lottery.LotteryStats
import java.io.File

/**
 * 777 statistics.
 *
 * @author Moshe
 */
class Lotto777Stats(lottery: Lottery) : LotteryStats(lottery, 17) {
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
        val thresholdCandidates = numBalls * THRESHOLD_CANDIDATES_PERCENT / 100
        val thresholdCandidatesOr4 = thresholdCandidates / 4
        val lotteryMin = lottery.minimum
        val lotteryMax = lottery.maximum
        val nstatRow: Array<NumberStatistic?> = numStats!![numStats!!.size - 1]
        val asJava = StringBuilder()
        var add: Boolean
        for (row in nstatRow) {
            val nstat = row!!
            println(nstat)
            add = nstat.id in lotteryMin..lotteryMax
            add = add and (nstat.repeat < maxRepeat)
            // Copy from Tester#nextCandidates and paste here:
            add =
                add and (nstat.indexMostUsed < thresholdCandidatesOr4 || nstat.indexLeastUsed < thresholdCandidatesOr4 || nstat.indexMostCount < thresholdCandidatesOr4 || nstat.indexLeastCount < thresholdCandidatesOr4)
            if (add) {
                if (asJava.isNotEmpty()) {
                    asJava.append(',')
                }
                asJava.append(nstat.id)
            }
        }
        println(asJava)
    }

    override fun createResultsReader(): LotteryResultsReader {
        return Lotto777ResultsReader()
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