package com.github.pnemonic.game.lottery

import com.github.pnemonic.game.NumberStatisticGrouping
import java.io.File
import java.io.IOException

abstract class LotteryTester<L: Lottery>(protected val lottery: L) {
    protected val lotterySize: Int = lottery.size

    protected val lotteryMin: Int = lottery.minimum

    protected val lotteryMax: Int = lottery.maximum

    protected val numBalls: Int = lottery.numberBalls

    protected var records: List<LotteryRecord> = emptyList()
        private set

    protected val candidates: MutableList<Int> = ArrayList(numBalls)

    @Throws(IOException::class)
    fun parse(file: File) {
        val reader: LotteryResultsReader = createResultsReader()
        records = reader.parse(file).sortedBy { it.id }
    }

    @Throws(IOException::class)
    protected abstract fun createResultsReader(): LotteryResultsReader

    fun drive() {
        val stats = createStats(lottery)
        stats.processRecords(records, false, true)
        for (grouping in NumberStatisticGrouping.entries) {
            drive(grouping, stats)
        }
    }

    protected abstract fun createStats(lottery: L): LotteryStats<L>

    protected abstract fun drive(grouping: NumberStatisticGrouping, stats: LotteryStats<L>)

    protected fun play(numGames: Int, record: LotteryRecord): List<LotteryGame> {
        val games = mutableListOf<LotteryGame>()
        var gamesSize = 0
        var retry = 10
        while (gamesSize < numGames) {
            games.addAll(lottery.play(numGames - gamesSize, record))
            gamesSize = games.size
            retry--
            if (retry == 0) {
                break
            }
        }
        return games
    }
}