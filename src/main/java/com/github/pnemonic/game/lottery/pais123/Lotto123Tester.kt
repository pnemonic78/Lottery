package com.github.pnemonic.game.lottery.pais123

import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryRecord
import com.github.pnemonic.game.lottery.LotteryResultsReader
import com.github.pnemonic.game.lottery.pais123.Lotto123.Companion.COST
import java.io.File
import java.io.IOException
import kotlin.math.floor

/**
 * Test various strategies for "123".
 */
class Lotto123Tester {
    private enum class CompareBy {
        REGULAR,
        LEAST_COUNT,
        MOST_COUNT,
        LEAST_USED,
        MOST_USED,
        LEAST_REPEAT,
        MOST_REPEAT,
        COUNT_223,
        COUNT_232,
        COUNT_322,
        LEAST_COUNT_LEAST_USED,
        LEAST_COUNT_MOST_USED,
        MOST_COUNT_LEAST_USED,
        MOST_COUNT_MOST_USED
    }

    private var records: List<LotteryRecord> = emptyList()
    private val lottery: Lottery = Lotto123()
    private val lotterySize: Int = lottery.size
    private val numBalls: Int = lottery.numberBalls
    private val thresholdCandidates: Int = (numBalls * THRESHOLD_CANDIDATES_PERCENT) / 100
    private val candidates: MutableList<Int> = ArrayList(numBalls)
    private var numStats: Array<Array<NumberStatistic?>>? = null

    init {
        require(thresholdCandidates >= lotterySize) { "too few candidates" }
    }

    @Throws(IOException::class)
    fun parse(file: File) {
        val reader: LotteryResultsReader = Lotto123ResultsReader()
        records = reader.parse(file)
    }

    fun drive() {
        val stats = Lotto123Stats(lottery)
        try {
            stats.processRecords(records, false, true)
            numStats = stats.numberStatistics
            driveRegular()
            driveLeastCount()
            driveMostCount()
            driveLeastUsed()
            driveMostUsed()
            // driveLeastRepeated();
            // driveMostRepeated();
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

    private fun driveLeastUsed() {
        drive(CompareBy.LEAST_USED, "least used")
    }

    private fun driveMostUsed() {
        drive(CompareBy.MOST_USED, "most used")
    }

    private fun driveLeastCount() {
        drive(CompareBy.LEAST_COUNT, "least count")
    }

    private fun driveMostCount() {
        drive(CompareBy.MOST_COUNT, "most count")
    }

    private fun driveLeastRepeated() {
        drive(CompareBy.LEAST_REPEAT, "least repeated")
    }

    private fun driveMostRepeated() {
        drive(CompareBy.MOST_REPEAT, "most repeated")
    }

    private fun driveRegular() {
        drive(CompareBy.REGULAR, "regular")
    }

    private fun driveCount223() {
        drive(CompareBy.COUNT_223, "2-2-3")
    }

    private fun driveCount232() {
        drive(CompareBy.COUNT_232, "2-3-2")
    }

    private fun driveCount322() {
        drive(CompareBy.COUNT_322, "3-2-2")
    }

    private fun driveLeastCountLeastUsed() {
        drive(CompareBy.LEAST_COUNT_LEAST_USED, "least count, least used")
    }

    private fun driveLeastCountMostUsed() {
        drive(CompareBy.LEAST_COUNT_MOST_USED, "least count, most used")
    }

    private fun driveMostCountLeastUsed() {
        drive(CompareBy.MOST_COUNT_LEAST_USED, "most count, least used")
    }

    private fun driveMostCountMostUsed() {
        drive(CompareBy.MOST_COUNT_MOST_USED, "most count, most used")
    }

    private fun drive(comparator: CompareBy, name: String) {
        val numPlays = floor(BUDGET / COST).toInt()
        var games: Collection<LotteryGame>
        var wallet = BUDGET
        var recordIndex = 0
        var numGamesTotal = 0
        candidates.clear()
        for (record in records) {
            lottery.setCandidates(candidates)
            wallet -= numPlays * COST
            games = play(numPlays/*, record*/)
            for (game in games) {
                wallet += game.prize
            }
            nextCandidates(comparator, recordIndex)
            recordIndex++
            numGamesTotal += games.size
        }
        val aveScore = wallet / numGamesTotal
        println("$name:\t{wallet $wallet; ave. $aveScore}")
    }

    private fun play(numGames: Int): List<LotteryGame> {
        val games = mutableSetOf<LotteryGame>()
        games.addAll(lottery.play(numGames))
        var gamesSize = games.size
        while (gamesSize in 2 until numGames) {
            games.addAll(lottery.play(numGames - gamesSize))
            gamesSize = games.size
        }
        return games.toList()
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
                CompareBy.LEAST_COUNT ->
                    add = add && (nstat.indexLeastCount < thresholdCandidates)

                CompareBy.LEAST_REPEAT ->
                    add = add && (nstat.indexLeastRepeat < thresholdCandidates)

                CompareBy.LEAST_USED ->
                    add = add && (nstat.indexLeastUsed < thresholdCandidates)

                CompareBy.MOST_COUNT ->
                    add = add && (nstat.indexMostCount < thresholdCandidates)

                CompareBy.MOST_REPEAT ->
                    add = add && (nstat.indexMostRepeat < thresholdCandidates)

                CompareBy.MOST_USED ->
                    add = add && (nstat.indexMostUsed < thresholdCandidates)

                CompareBy.LEAST_COUNT_LEAST_USED -> {
                    add = add && (nstat.indexLeastCount < thresholdCandidates)
                    add = add && (nstat.indexLeastUsed < thresholdCandidates)
                }

                CompareBy.LEAST_COUNT_MOST_USED -> {
                    add = add && (nstat.indexLeastCount < thresholdCandidates)
                    add = add && (nstat.indexMostUsed < thresholdCandidates)
                }

                CompareBy.MOST_COUNT_LEAST_USED -> {
                    add = add && (nstat.indexMostCount < thresholdCandidates)
                    add = add && (nstat.indexLeastUsed < thresholdCandidates)
                }

                CompareBy.MOST_COUNT_MOST_USED -> {
                    add = add && (nstat.indexMostCount < thresholdCandidates)
                    add = add && (nstat.indexMostUsed < thresholdCandidates)
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

        /**
         * Maximum repeat of same number, even though statistically maximum is 9.
         */
        private const val MAX_REPEAT_THRESHOLD = 8

        /**
         * Budget.
         */
        internal const val BUDGET = 20.00
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
    val tester = Lotto123Tester()
    try {
        tester.parse(file)
        tester.drive()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}