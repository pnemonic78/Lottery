package com.github.pnemonic.game.lottery

import com.github.pnemonic.game.CompareByCount
import com.github.pnemonic.game.CompareById
import com.github.pnemonic.game.CompareByUsage
import com.github.pnemonic.game.CompareNumber
import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.RecordStatistic
import com.github.pnemonic.isEven
import java.io.File
import java.io.IOException
import kotlin.math.max
import kotlin.math.min

abstract class LotteryStats<L : Lottery>(protected val lottery: L) {

    protected val lotteryMin: Int = lottery.minimum
    protected val lotteryMax: Int = lottery.maximum

    protected val numBalls: Int = lottery.numberBalls

    protected val recStats = mutableListOf<RecordStatistic>()

    /**
     * Get the number statistics. Each row represents a game. Each column
     * represents a ball.
     */
    var numberStatistics: Array<Array<NumberStatistic>> = emptyArray()
        private set

    /**
     * The upper bound of the minimum balls.
     */
    protected var maxLower = Int.MIN_VALUE

    /**
     * The lower bound of the maximum balls.
     */
    protected var minUpper = Int.MAX_VALUE

    /**
     * The widest gap between any 2 balls.
     */
    protected var maxGap = 0

    /**
     * The lowest number of odd balls.
     */
    protected var minOdd = Int.MAX_VALUE

    /**
     * The lowest number of even balls.
     */
    protected var minEven = Int.MAX_VALUE

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

    @Throws(IOException::class)
    fun parse(file: File) {
        val reader = createResultsReader()
        val records = reader.parse(file)
        processRecords(records)
        printRecordStats()
        printNumberStats()
    }

    fun processRecords(
        records: List<LotteryRecord>,
        processRecordStatistics: Boolean = true,
        processNumberStatistics: Boolean = true
    ) {
        recStats.clear()
        numberStatistics =
            Array(records.size) { Array(numBalls) { NumberStatistic(it + lotteryMin) } }
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
        val numStats = this.numberStatistics
        var rstat: RecordStatistic
        var nstat: NumberStatistic
        var nstatPrev: NumberStatistic
        var numStatsRow: Array<NumberStatistic>
        var numStatsRowPrev: Array<NumberStatistic>? = null
        val byLeastCount: CompareNumber = CompareByCount(false)
        val byLeastUsed: CompareNumber = CompareByUsage(false)
        val byId: CompareNumber = CompareById(false)
        var row = 0

        for (record in records) {
            val recordBalls = record.balls
            val numRecordBalls = recordBalls.size
            numStatsRow = numStats[row]

            if (processRecordStatistics) {
                maxLower = max(maxLower, recordBalls[0])
                for (i in numRecordBalls - 1 downTo 1) {
                    val n = recordBalls[i]
                    if (n in lotteryMin..lotteryMax) {
                        minUpper = min(minUpper, n)
                        break
                    }
                }
                rstat = addStat(record)
                for (i in rstat.gap.indices) {
                    val gap = rstat.gap[i]
                    val n0 = recordBalls[i]
                    val n1 = n0 + gap
                    if (n0 in lotteryMin..lotteryMax && n1 in lotteryMin..lotteryMax) {
                        maxGap = max(maxGap, gap)
                    }
                }
            }

            if (processNumberStatistics) {
                for (ball in recordBalls) {
                    val col = ball - lotteryMin
                    nstat = numStatsRow[col]
                    nstat.countGame++
                }
                if (numStatsRowPrev != null) {
                    for (col in 0 until numBalls) {
                        nstat = numStatsRow[col]
                        nstatPrev = numStatsRowPrev[col]
                        nstat.count = nstatPrev.count + nstat.countGame
                        nstat.usage = (nstatPrev.usage + nstat.countGame) / 2
                        if (nstat.countGame > 0) {
                            nstat.repeat = nstatPrev.repeat + nstat.countGame
                        }
                        nstat.maxRepeat = max(nstat.repeat, nstatPrev.maxRepeat)
                        maxRepeat = max(maxRepeat, nstat.maxRepeat)
                    }
                } else {
                    for (col in 0 until numBalls) {
                        nstat = numStatsRow[col]
                        nstat.count += nstat.countGame
                        nstat.usage = nstat.countGame.toFloat()
                        nstat.repeat = nstat.countGame
                        nstat.maxRepeat = max(nstat.repeat, nstat.maxRepeat)
                        maxRepeat = max(maxRepeat, nstat.maxRepeat)
                    }
                }
                numStatsRow.sortWith(byLeastCount)
                for (col in 0 until numBalls) {
                    nstat = numStatsRow[col]
                    nstat.indexLeastCount = col
                    nstat.indexMostCount = numBalls - col - 1
                }
                for (col in 0 until numBalls) {
                    nstat = numStatsRow[col]
                    nstat.indexLeastUsed = col
                    nstat.indexMostUsed = numBalls - col - 1
                }
                numStatsRow.sortWith(byId)

                var col2: Int
                var pair: Int
                var pair2: Int
                var pairIndex: Int
                var pairIndex2: Int
                var col = 0
                while (col < recordBalls.size) {
                    pair = recordBalls[col]
                    if (pair in lotteryMin..lotteryMax) {
                        pairIndex = pair - lotteryMin
                        col2 = col + 1
                        if (col2 < recordBalls.size) {
                            pair2 = recordBalls[col2]
                            if (pair2 in lotteryMin..lotteryMax) {
                                pairIndex2 = pair2 - lotteryMin
                                numPairs[pairIndex][pairIndex2]++
                                numPairs[pairIndex2][pairIndex]++
                            }
                        }
                    }
                    col++
                }
            }
            numStatsRowPrev = numStatsRow
            row++
        }
    }

    protected fun addStat(record: LotteryRecord): RecordStatistic {
        val recordBalls = record.balls
        val size = recordBalls.size
        val sizeConsecutives = size - 1
        val rstat = RecordStatistic(record)
        val con = BooleanArray(sizeConsecutives)
        var i = 0
        for (i1 in 1..con.size) {
            con[i] = recordBalls[i] + 1 == recordBalls[i1]
            rstat.gap[i] = recordBalls[i1] - recordBalls[i]
            i++
        }
        i = 0
        for (i1 in 1 until size) {
            var c = true
            var j = i
            var j1 = j + 1
            var s = 0
            while (c && j1 < size) {
                c = con[j]
                rstat.consecutive[s] += if (c) 1 else 0
                j++
                j1++
                s++
            }
            i++
        }
        for (ball in recordBalls) {
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
            print(rstat.record.id)
            for (c in rstat.consecutive) {
                print("\t$c")
            }
            print("\te: ${rstat.even}")
            print("\to: ${rstat.odd}")
            println()
        }
    }

    protected open fun printNumberStats() = Unit
}