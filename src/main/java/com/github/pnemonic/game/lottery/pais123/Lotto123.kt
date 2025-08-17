package com.github.pnemonic.game.lottery.pais123

import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryGuess
import com.github.pnemonic.game.lottery.pais123.Lotto123.Companion.COST
import com.github.pnemonic.game.lottery.pais123.Lotto123Tester.Companion.BUDGET
import kotlin.math.floor

/**
 * Choose numbers for 123 in Israel.
 */
class Lotto123 : Lottery(SIZE) {
    override val minimum: Int = 0
    override val maximum: Int = 9
    override val bonusMinimum: Int = 0
    override val bonusMaximum: Int = 0

    override fun calculatePrizes(guess: LotteryGuess, result: LotteryGame) {
        var match = 0
        for (i in 0 until SIZE) {
            if (guess.balls[i] == result.balls[i]) {
                match++
            }
        }

        when (match) {
            SIZE -> result.prize = PRIZE
            else -> result.prize = 0
        }
    }

    companion object {
        private const val SIZE = 1

        /**
         * Cost per game.
         */
        const val COST = 1.00

        /** Prize for guessing the 3 correct numbers. */
        const val PRIZE = 600
    }
}

/**
 * Main method.
 *
 * @param args the array of arguments.
 */
fun main(args: Array<String>) {
    val lottery = Lotto123()
    if (args.isNotEmpty()) {
        lottery.setCandidates(args[0])
    }
    val numPlays = floor(BUDGET / COST).toInt()
    val games = lottery.play(numPlays)
    for (game in games) {
        lottery.print(game)
    }
}