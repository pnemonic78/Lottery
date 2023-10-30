package com.github.pnemonic.game

import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryRecord
import java.io.File
import java.io.IOException

abstract class Tester(protected val lottery: Lottery) {
    protected val lotterySize: Int = lottery.size

    protected val lotteryMin: Int = lottery.minimum

    protected val lotteryMax: Int = lottery.maximum

    protected val numBalls: Int = lottery.numberBalls

    protected var records: List<LotteryRecord> = emptyList()

    protected var recordsSize = 0

    protected var numGamesTotal = 0

    protected var numStats: Array<Array<NumberStatistic?>>? = null

    @Throws(IOException::class)
    abstract fun parse(file: File)

    abstract fun drive()

    protected abstract fun drive(grouping: NumberStatisticGrouping, name: String)

    protected fun play(numGames: Int): Set<LotteryGame> {
        val games = lottery.play(numGames)
        var gamesSize = games.size
        var retry = 10
        while (gamesSize in 2 until numGames) {
            games.addAll(lottery.play(numGames - gamesSize))
            gamesSize = games.size
            retry--
            if (retry == 0) {
                break
            }
        }
        return games
    }
}