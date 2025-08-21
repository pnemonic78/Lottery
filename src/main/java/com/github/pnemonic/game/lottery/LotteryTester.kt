package com.github.pnemonic.game.lottery

import com.github.pnemonic.game.NumberStatistic
import com.github.pnemonic.game.NumberStatisticGrouping
import java.io.File
import java.io.IOException

abstract class LotteryTester(protected val lottery: Lottery) {
    protected val lotterySize: Int = lottery.size

    protected val lotteryMin: Int = lottery.minimum

    protected val lotteryMax: Int = lottery.maximum

    protected val numBalls: Int = lottery.numberBalls

    protected var records: List<LotteryRecord> = emptyList()
        private set

    protected var numStats: Array<Array<NumberStatistic?>> = emptyArray()

    @Throws(IOException::class)
    fun parse(file: File) {
        val reader: LotteryResultsReader = createResultsReader()
        records = reader.parse(file).sortedBy { it.id }
    }

    @Throws(IOException::class)
    protected abstract fun createResultsReader(): LotteryResultsReader

    abstract fun drive()

    protected abstract fun drive(grouping: NumberStatisticGrouping)

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