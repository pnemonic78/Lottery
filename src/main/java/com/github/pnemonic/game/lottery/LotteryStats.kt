package com.github.pnemonic.game.lottery

import com.github.pnemonic.game.CompareByCount
import com.github.pnemonic.game.CompareByUsage
import com.github.pnemonic.game.CompareNumber
import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.RecordStatistic
import java.io.File
import java.util.Arrays
import kotlin.math.max
import kotlin.math.min

abstract class LotteryStats(
    protected val lottery: Lottery,
    /**
     * The number of balls per record.
     */
    protected val numRecordBalls: Int
) {
    protected var records: List<LotteryRecord>? = null
    protected val recStats: MutableList<RecordStatistic> = ArrayList()

    /**
     * Get the number statistics. Each row represents a game. Each column
     * represents a ball.
     *
     * @return
     */
    var numStats: Array<Array<NumberStatistic?>>? = null
    //FIXME protected set

    /**
     * The upper bound of the minimum balls.
     */
    protected var maxLower = 0

    /**
     * The lower bound of the maximum balls.
     */
    protected var minUpper = 0

    /**
     * The widest gap between any 2 balls.
     */
    protected var maxGap = 0
    protected val lotteryMin: Int = lottery.minimum
    protected val lotteryMax: Int = lottery.maximum

    /**
     * The number of balls.
     */
    protected val numBalls: Int = lottery.numberBalls

    /**
     * The lowest number of odd balls.
     */
    protected var minOdd = 0

    /**
     * The lowest number of even balls.
     */
    protected var minEven = 0

    /**
     * The highest number of odd balls.
     */
    protected var maxOdd = 0

    /**
     * The highest number of even balls.
     */
    protected var maxEven = 0

    /**
     * The highest number of repeated balls.
     */
    protected var maxRepeat = 0

    /**
     * The number of occurrences of pairs.
     */
    protected var numPairs: Array<IntArray>? = null

    protected abstract fun createResultsReader(): LotteryResultsReader

    fun parse(file: File) {
        val reader = createResultsReader()
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
        numStats = Array(records.size) { arrayOfNulls(numBalls) }
        maxLower = Int.MIN_VALUE
        minUpper = Int.MAX_VALUE
        maxGap = 0
        minOdd = Int.MAX_VALUE
        maxOdd = 0
        minEven = Int.MAX_VALUE
        maxEven = 0
        maxRepeat = 0
        val numPairs = Array(numBalls) { IntArray(numBalls) }
        this.numPairs = numPairs
        val numStats = this.numStats!!
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
        var col: Int
        for (record in records) {
            numStatsRow = numStats[row]
            row_1 = row - 1
            if (processRecordStatistics) {
                maxLower = max(maxLower, record.lot[0])
                for (i in numRecordBalls - 1 downTo 1) {
                    val n = record.lot[i]
                    if (n in lotteryMin..lotteryMax) {
                        minUpper = min(minUpper, n)
                        break
                    }
                }
                rstat = addStat(record)
                for (i in rstat.gap.indices) {
                    val gap = rstat.gap[i]
                    val n0 = record.lot[i]
                    val n1 = n0 + gap
                    if (n0 in lotteryMin..lotteryMax && n1 in lotteryMin..lotteryMax) {
                        maxGap = max(maxGap, gap)
                    }
                }
            }
            if (processNumberStatistics) {
                col = 0
                for (n in 1..numBalls) {
                    nstat = NumberStatistic()
                    nstat.id = n
                    numStatsRow[col] = nstat
                    col++
                }
                var col2: Int
                var pair: Int
                var pair2: Int
                var pairIndex: Int
                var pairIndex2: Int
                col = 0
                while (col < record.lot.size) {
                    pair = record.lot[col]
                    if (pair in lotteryMin..lotteryMax) {
                        pairIndex = pair - lotteryMin
                        col2 = col + 1
                        if (col2 < record.lot.size) {
                            pair2 = record.lot[col2]
                            if (pair2 in lotteryMin..lotteryMax) {
                                pairIndex2 = pair2 - lotteryMin
                                numPairs[pairIndex][pairIndex2]++
                                numPairs[pairIndex2][pairIndex]++
                            }
                        }
                    }
                    col++
                }
                for (n in record.lot) {
                    if (n in lotteryMin..lotteryMax) {
                        col = n - lotteryMin
                        nstat = numStatsRow[col]!!
                        nstat.occur++
                    }
                }
                if (row_1 >= 0) {
                    numStatsRow_1 = numStats[row_1]
                    col = 0
                    var n = 1
                    while (n <= numBalls) {
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
                        n++
                    }
                } else {
                    col = 0
                    var n = 1
                    while (n <= numBalls) {
                        nstat = numStatsRow[col]!!
                        nstat.count += nstat.occur
                        nstat.usage = nstat.occur.toFloat()
                        nstat.repeat = nstat.occur
                        nstat.maxRepeat = max(nstat.repeat, nstat.maxRepeat)
                        maxRepeat = max(maxRepeat, nstat.maxRepeat)
                        col++
                        n++
                    }
                }
                System.arraycopy(numStatsRow, 0, numStatsRowSorted, 0, numBalls)
                Arrays.sort(numStatsRowSorted, forLeastCount)
                col = 0
                run {
                    var n = 1
                    while (n <= numBalls) {
                        nstat = numStatsRowSorted[col]!!
                        nstat.indexLeastCount = col
                        nstat.indexMostCount = numBalls - col - 1
                        col++
                        n++
                    }
                }
                Arrays.sort(numStatsRowSorted, forLeastUsed)
                col = 0
                var n = 1
                while (n <= numBalls) {
                    nstat = numStatsRowSorted[col]!!
                    nstat.indexLeastUsed = col
                    nstat.indexMostUsed = numBalls - col - 1
                    col++
                    n++
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
        run {
            var i = 0
            var i1 = 1
            while (i1 <= con.size) {
                con[i] = record.lot[i] + 1 == record.lot[i1]
                rstat.gap[i] = record.lot[i1] - record.lot[i]
                i++
                i1++
            }
        }
        var c: Boolean
        var i = 0
        var i1 = 1
        while (i1 < size) {
            c = true
            var j = i
            var j1 = j + 1
            var s = 0
            while (c && j1 < size) {
                c = c and con[j]
                rstat.consecutive[s] += if (c) 1 else 0
                j++
                j1++
                s++
            }
            i++
            i1++
        }
        for (ball in record.lot) {
            if (ball and 1 == 0) {
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
                print("\t" + c)
            }
            print("\te:" + rstat.even)
            print("\to:" + rstat.odd)
            println()
        }
    }

    protected open fun printNumberStats() = Unit
}