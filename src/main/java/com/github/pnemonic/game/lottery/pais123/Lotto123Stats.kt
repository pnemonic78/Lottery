package com.github.pnemonic.game.lottery.pais123

import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.lottery.LotteryRecord
import com.github.pnemonic.game.lottery.LotteryResultsReader
import com.github.pnemonic.game.lottery.LotteryStats
import com.github.pnemonic.game.lottery.NumberStatisticsGames
import com.github.pnemonic.game.lottery.NumberStatisticsRow
import java.io.File

/**
 * 123 statistics.
 */
class Lotto123Stats(lottery: Lotto123) : LotteryStats<Lotto123>(lottery) {
    internal var numberStatisticsTens: NumberStatisticsGames = emptyArray()
    internal var numberStatisticsHundreds: NumberStatisticsGames = emptyArray()

    override fun createResultsReader(): LotteryResultsReader {
        return Lotto123ResultsReader()
    }

    override fun processRecords(
        records: List<LotteryRecord>,
        processRecordStatistics: Boolean,
        processNumberStatistics: Boolean
    ) {
        super.processRecords(records, processRecordStatistics, processNumberStatistics)
        processRecords(
            records,
            numberStatisticsTens,
            processRecordStatistics,
            processNumberStatistics
        )
        processRecords(
            records,
            numberStatisticsHundreds,
            processRecordStatistics,
            processNumberStatistics
        )
    }

    override fun generateNumberStatistics(recordsSize: Int, numBalls: Int) {
        super.generateNumberStatistics(recordsSize, numBalls)
        numberStatisticsTens =
            Array(recordsSize) { Array(numBalls) { NumberStatistic(it + lotteryMin) } }
        numberStatisticsHundreds =
            Array(recordsSize) { Array(numBalls) { NumberStatistic(it + lotteryMin) } }
    }

    override fun incrementBallCounter(
        recordBalls: IntArray,
        numStats: NumberStatisticsGames,
        numStatsRow: NumberStatisticsRow
    ) {
        val ball = if (numStats == numberStatistics) {
            recordBalls[0]
        } else if (numStats == numberStatisticsTens) {
            recordBalls[1]
        } else {
            recordBalls[2]
        }
        val col = ball - lotteryMin
        val nstat = numStatsRow[col]
        nstat.countGame++
    }

    override fun printNumberStats() {
        println("max. repeat: $maxRepeat")
        val threshold = 35
        val numStats = numberStatistics
        val nstatRow = numStats[numStats.lastIndex]
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