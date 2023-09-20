package com.github.pnemonic.game

import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryRecord
import java.io.File
import java.io.IOException

abstract class Tester(@JvmField protected val lottery: Lottery) {
    @JvmField
    protected val lotterySize: Int = lottery.size

    @JvmField
    protected val lotteryMin: Int = lottery.minimum

    @JvmField
    protected val lotteryMax: Int = lottery.maximum

    @JvmField
    protected val numBalls: Int = lottery.numberBalls

    @JvmField
    protected var records: List<LotteryRecord> = emptyList()

    @JvmField
    protected var recordsSize = 0

    @JvmField
    protected var numGamesTotal = 0

    @JvmField
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