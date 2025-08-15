package com.github.pnemonic.game.lottery.pais123

import com.github.pnemonic.game.CompareByCount
import com.github.pnemonic.game.CompareByUsage
import com.github.pnemonic.game.CompareNumber
import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.RecordStatistic
import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryRecord
import com.github.pnemonic.game.lottery.LotteryResultsReader
import com.github.pnemonic.isEven
import java.io.File
import kotlin.math.max
import kotlin.math.min

/**
 * 123 statistics.
 */
class Lotto123Stats(lottery: Lottery) {
    private var records: List<LotteryRecord>? = null
    private val recStats = mutableListOf<RecordStatistic>()

    /**
     * Get the number statistics. Each row represents a game. Each column
     * represents a ball.
     *
     * @return
     */
    var numberStatistics: Array<Array<NumberStatistic?>>? = null
    //FIXME private set

    /**
     * The upper bound of the minimum balls.
     */
    private var maxLower = 0

    /**
     * The lower bound of the maximum balls.
     */
    private var minUpper = 0

    /**
     * The widest gap between any 2 balls.
     */
    private var maxGap = 0

    /**
     * The number of balls.
     */
    private val numBalls: Int = lottery.numberBalls

    /**
     * The lowest number of odd balls.
     */
    private var minOdd = 0

    /**
     * The lowest number of even balls.
     */
    private var minEven = 0

    /**
     * The highest number of odd balls.
     */
    private var maxOdd = 0

    /**
     * The highest number of even balls.
     */
    private var maxEven = 0

    /**
     * The highest number of repeated balls.
     */
    private var maxRepeat = 0

    /**
     * The number of balls per record.
     */
    private val numRecordBalls = 3

    fun parse(file: File) {
        val reader: LotteryResultsReader = Lotto123ResultsReader()
        try {
            records = reader.parse(file)
            processRecords(records!!)
            printRecordStats()
            printNumberStats()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun processRecords(
        records: List<LotteryRecord>,
        processRecordStatistics: Boolean = true,
        processNumberStatistics: Boolean = true
    ) {
        recStats.clear()
        numberStatistics = Array(records.size) { arrayOfNulls(numBalls) }
        maxLower = Int.MIN_VALUE
        minUpper = Int.MAX_VALUE
        maxGap = 0
        minOdd = Int.MAX_VALUE
        maxOdd = 0
        minEven = Int.MAX_VALUE
        maxEven = 0
        maxRepeat = 0
        var rstat: RecordStatistic
        var nstat: NumberStatistic
        var nstatPrev: NumberStatistic
        var numStatsRow: Array<NumberStatistic?>
        var numStatsRow_1: Array<NumberStatistic?>
        val numStatsRowSorted = arrayOfNulls<NumberStatistic>(numBalls)
        val forLeastCount: CompareNumber = CompareByCount(false)
        val forLeastUsed: CompareNumber = CompareByUsage(false)
        var row = 0
        var row_1: Int
        for (record in records) {
            numStatsRow = numberStatistics!![row]
            row_1 = row - 1
            if (processRecordStatistics) {
                for (ball in record.lot) {
                    maxLower = max(maxLower, ball)
                    minUpper = min(minUpper, ball)
                }
                rstat = addStat(record)
                for (gap in rstat.gap) {
                    maxGap = max(maxGap, gap)
                }
            }
            if (processNumberStatistics) {
                var col = 0
                for (n in 1..numBalls) {
                    nstat = NumberStatistic()
                    nstat.id = n
                    numStatsRow[col] = nstat
                    col++
                }
                for (n in record.lot) {
                    nstat = numStatsRow[n - 1]!!
                    nstat.occur++
                }
                if (row_1 >= 0) {
                    numStatsRow_1 = numberStatistics!![row_1]
                    col = 0
                    for (n in 1..numBalls) {
                        nstat = numStatsRow[col]!!
                        nstatPrev = numStatsRow_1[col]!!
                        nstat.count = nstatPrev.count + nstat.occur
                        nstat.usage = (nstatPrev.usage + nstat.occur) / 2
                        if (nstat.occur > 0) {
                            nstat.repeat = nstatPrev.repeat + nstat.occur
                        }
                        nstat.maxRepeat = max(nstat.repeat, nstatPrev.maxRepeat)
                        maxRepeat = max(maxRepeat, nstat.maxRepeat)
                        col++
                    }
                } else {
                    col = 0
                    for (n in 1..numBalls) {
                        nstat = numStatsRow[col]!!
                        nstat.count += nstat.occur
                        nstat.usage = nstat.occur.toFloat()
                        nstat.repeat = nstat.occur
                        nstat.maxRepeat = max(nstat.repeat, nstat.maxRepeat)
                        maxRepeat = max(maxRepeat, nstat.maxRepeat)
                        col++
                    }
                }
                numStatsRow.copyInto(numStatsRowSorted, 0, 0, numBalls)
                numStatsRowSorted.sortWith(forLeastCount)
                col = 0
                for (n in 1..numBalls) {
                    nstat = numStatsRowSorted[col]!!
                    nstat.indexLeastCount = col
                    nstat.indexMostCount = numBalls - col - 1
                    col++
                }
                numStatsRowSorted.sortWith(forLeastUsed)
                col = 0
                for (n in 1..numBalls) {
                    nstat = numStatsRowSorted[col]!!
                    nstat.indexLeastUsed = col
                    nstat.indexMostUsed = numBalls - col - 1
                    col++
                }
            }
            row++
        }
    }

    protected fun addStat(record: LotteryRecord): RecordStatistic {
        val size = numRecordBalls
        val sizeConsecutives = size - 1
        val rstat = RecordStatistic(sizeConsecutives)
        rstat.record = record
        val con = BooleanArray(sizeConsecutives)
        var i = 0
        for (i1 in 1..con.size) {
            con[i] = record.lot[i] + 1 == record.lot[i1]
            rstat.gap[i] = record.lot[i1] - record.lot[i]
            i++
        }
        var c: Boolean
        i = 0
        for (i1 in 1 until size) {
            c = true
            var j = i
            var j1 = j + 1
            var s = 0
            while (c && j1 < size) {
                c = c && con[j]
                rstat.consecutive[s] += if (c) 1 else 0
                j++
                j1++
                s++
            }
            i++
        }
        for (ball in record.lot) {
            if (ball.isEven) {
                rstat.even++
            } else {
                rstat.odd++
            }
        }
        minOdd = min(minOdd, rstat.odd)
        maxOdd = max(maxOdd, rstat.odd)
        minEven = min(minEven, rstat.even)
        maxEven = max(maxEven, rstat.even)
        recStats.add(rstat)
        return rstat
    }

    protected fun printRecordStats() {
        println("max. lower: $maxLower")
        println("min. upper: $minUpper")
        println("max. gap: $maxGap")
        println("min. odd: $minOdd")
        println("max. odd: $maxOdd")
        println("min. even: $minEven")
        println("max. even: $maxEven")
        for (rstat in recStats) {
            print(rstat.record!!.id)
            for (c in rstat.consecutive) {
                print("\t$c")
            }
            print("\te: ${rstat.even}")
            print("\to: ${rstat.odd}")
            println()
        }
    }

    protected fun printNumberStats() {
        println("max. repeat: $maxRepeat")
        val threshold = 35
        val nstatRow: Array<NumberStatistic?> = numberStatistics!![numberStatistics!!.size - 1]
        val asJava = StringBuffer()
        for (nr in nstatRow) {
            val stat = nr!!
            println(stat)
            if (stat.repeat < maxRepeat && stat.indexMostCount < threshold && stat.indexLeastUsed < threshold) {
                if (asJava.isNotEmpty()) {
                    asJava.append(',')
                }
                asJava.append(stat.id)
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