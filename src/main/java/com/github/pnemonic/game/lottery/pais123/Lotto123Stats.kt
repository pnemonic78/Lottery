package com.github.pnemonic.game.lottery.pais123

import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.lottery.LotteryResultsReader
import com.github.pnemonic.game.lottery.LotteryStats
import java.io.File

/**
 * 123 statistics.
 */
class Lotto123Stats(lottery: Lotto123): LotteryStats<Lotto123>(lottery) {
    override fun createResultsReader(): LotteryResultsReader {
        return Lotto123ResultsReader()
    }

    override fun printNumberStats() {
        println("max. repeat: $maxRepeat")
        val threshold = 35
        val numStats = numberStatistics
        val nstatRow: Array<NumberStatistic> = numStats[numStats.lastIndex]
        val asJava = StringBuffer()
        for (nstat in nstatRow) {
            println(nstat)
            if (nstat.repeat < maxRepeat && nstat.indexMostCount < threshold && nstat.indexLeastUsed < threshold) {
                if (asJava.isNotEmpty()) {
                    asJava.append(',')
                }
                asJava.append(nstat.id)
            }
        }
        println(asJava)
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
    val stats = Lotto123Stats(Lotto123())
    stats.parse(file)
}