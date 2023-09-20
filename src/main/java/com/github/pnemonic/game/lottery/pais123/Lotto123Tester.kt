package com.github.pnemonic.game.lottery.pais123

import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryRecord
import com.github.pnemonic.game.lottery.LotteryResultsReader
import java.io.File
import java.io.IOException

/**
 * Test various strategies for "123".
 *
 * @author moshew
 */
class Lotto123Tester {
    private enum class CompareBy {
        REGULAR, LEAST_COUNT, MOST_COUNT, LEAST_USED, MOST_USED, LEAST_REPEAT, MOST_REPEAT, COUNT_223, COUNT_232, COUNT_322, LEAST_COUNT_LEAST_USED, LEAST_COUNT_MOST_USED, MOST_COUNT_LEAST_USED, MOST_COUNT_MOST_USED
    }

    private var records: List<LotteryRecord>? = null
    private val lottery: Lottery = Lotto123()
    private val lotterySize: Int = lottery.size
    private val lotteryMin: Int = lottery.minimum
    private val lotteryMax: Int = lottery.maximum
    private val numBalls: Int = lottery.numberBalls
    private val thresholdCandidates: Int = numBalls * THRESHOLD_CANDIDATES_PERCENT / 100
    private val candidates: MutableList<Int> = ArrayList(numBalls)
    private var numStats: Array<Array<NumberStatistic?>>? = null
    private var numGamesTotal = 0

    init {
        require(thresholdCandidates >= lotterySize) { "too few candidates" }
    }

    @Throws(IOException::class)
    fun parse(file: File) {
        val reader: LotteryResultsReader = Lotto123ResultsReader()
        val records = reader.parse(file)
        this.records = records
        numGamesTotal = records.size * Lotto123.PLAYS
    }

    fun drive() {
        val stats = Lotto123Stats(lottery)
        try {
            stats.processRecords(records!!, false, true)
            numStats = stats.numberStatistics
            driveRegular()
            driveLeastCount()
            driveMostCount()
            driveLeastUsed()
            driveMostUsed()
            // // driveLeastRepeated();
            // // driveMostRepeated();
            driveLeastCountLeastUsed()
            driveLeastCountMostUsed()
            driveMostCountLeastUsed()
            driveMostCountMostUsed()
            // driveCount223();
            // driveCount232();
            // driveCount322();
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun driveLeastUsed() {
        drive(CompareBy.LEAST_USED, "least used")
    }

    protected fun driveMostUsed() {
        drive(CompareBy.MOST_USED, "most used")
    }

    protected fun driveLeastCount() {
        drive(CompareBy.LEAST_COUNT, "least count")
    }

    protected fun driveMostCount() {
        drive(CompareBy.MOST_COUNT, "most count")
    }

    protected fun driveLeastRepeated() {
        drive(CompareBy.LEAST_REPEAT, "least repeated")
    }

    protected fun driveMostRepeated() {
        drive(CompareBy.MOST_REPEAT, "most repeated")
    }

    protected fun driveRegular() {
        drive(CompareBy.REGULAR, "regular")
    }

    protected fun driveCount223() {
        drive(CompareBy.COUNT_223, "2-2-3")
    }

    protected fun driveCount232() {
        drive(CompareBy.COUNT_232, "2-3-2")
    }

    protected fun driveCount322() {
        drive(CompareBy.COUNT_322, "3-2-2")
    }

    protected fun driveLeastCountLeastUsed() {
        drive(CompareBy.LEAST_COUNT_LEAST_USED, "least count, least used")
    }

    protected fun driveLeastCountMostUsed() {
        drive(CompareBy.LEAST_COUNT_MOST_USED, "least count, most used")
    }

    protected fun driveMostCountLeastUsed() {
        drive(CompareBy.MOST_COUNT_LEAST_USED, "most count, least used")
    }

    protected fun driveMostCountMostUsed() {
        drive(CompareBy.MOST_COUNT_MOST_USED, "most count, most used")
    }

    private fun drive(comparator: CompareBy, name: String) {
        var games: Set<LotteryGame>
        var score: Int
        var maxScore = 0
        var totScore = 0
        var win = 0
        var recordIndex = 0
        candidates.clear()
        val records = this.records!!
        for (record in records) {
            lottery.setCandidates(candidates)
            games = play(Lotto123.PLAYS)
            for (game in games) {
                score = record.compareTo(game)
                totScore += score
                maxScore = score.coerceAtLeast(maxScore)
            }
            win += if (maxScore == WIN) 1 else 0
            nextCandidates(comparator, recordIndex)
            recordIndex++
        }
        val aveScore = totScore / numGamesTotal
        val winPercent = win.toFloat() / records.size * 100
        println("$name:\t{max. $maxScore;\tave. $aveScore;\twin $winPercent%}")
    }

    protected fun play(numGames: Int): Set<LotteryGame> {
        val games: MutableSet<LotteryGame> = lottery.play(numGames)
        var gamesSize = games.size
        while (gamesSize in 2 until numGames) {
            games.addAll(lottery.play(numGames - gamesSize))
            gamesSize = games.size
        }
        return games
    }

    /**
     * Use statistics to determine the next candidates.
     */
    private fun nextCandidates(comparator: CompareBy, row: Int) {
        // Get the statistics.
        val nstatRow = numStats?.get(row) ?: return
        candidates.clear()
        var add: Boolean
        for (nr in nstatRow) {
            val nstat = nr!!
            add = nstat.repeat < MAX_REPEAT_THRESHOLD
            when (comparator) {
                CompareBy.LEAST_COUNT -> add = add and (nstat.indexLeastCount < thresholdCandidates)
                CompareBy.LEAST_REPEAT -> add =
                    add and (nstat.indexLeastRepeat < thresholdCandidates)

                CompareBy.LEAST_USED -> add = add and (nstat.indexLeastUsed < thresholdCandidates)
                CompareBy.MOST_COUNT -> add = add and (nstat.indexMostCount < thresholdCandidates)
                CompareBy.MOST_REPEAT -> add = add and (nstat.indexMostRepeat < thresholdCandidates)
                CompareBy.MOST_USED -> add = add and (nstat.indexMostUsed < thresholdCandidates)
                CompareBy.LEAST_COUNT_LEAST_USED -> {
                    add = add and (nstat.indexLeastCount < thresholdCandidates)
                    add = add and (nstat.indexLeastUsed < thresholdCandidates)
                }

                CompareBy.LEAST_COUNT_MOST_USED -> {
                    add = add and (nstat.indexLeastCount < thresholdCandidates)
                    add = add and (nstat.indexMostUsed < thresholdCandidates)
                }

                CompareBy.MOST_COUNT_LEAST_USED -> {
                    add = add and (nstat.indexMostCount < thresholdCandidates)
                    add = add and (nstat.indexLeastUsed < thresholdCandidates)
                }

                CompareBy.MOST_COUNT_MOST_USED -> {
                    add = add and (nstat.indexMostCount < thresholdCandidates)
                    add = add and (nstat.indexMostUsed < thresholdCandidates)
                }

                else -> Unit
            }
            if (add) {
                candidates.add(nstat.id)
            }
        }
    }

    companion object {
        // per hundred
        private const val THRESHOLD_CANDIDATES_PERCENT = 50
        private const val WIN = 300

        /**
         * Maximum repeat of same number, even though statistically maximum is 9.
         */
        private const val MAX_REPEAT_THRESHOLD = 8

        /**
         * Main method.
         *
         * @param args the array of arguments.
         */
        fun main(args: Array<String>) {
            val fileName = if (args.isEmpty()) "results/777.csv" else args[0]
            val file = File(fileName)
            val tester = Lotto123Tester()
            try {
                tester.parse(file)
                tester.drive()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}