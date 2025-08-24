package com.github.pnemonic.game.lottery.pais123

import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryGuess
import com.github.pnemonic.game.lottery.pais123.Lotto123.Companion.COST
import com.github.pnemonic.game.lottery.pais123.Lotto123Tester.Companion.BUDGET

/**
 * Choose numbers for 123 in Israel.
 * Results are from `0-0-0` to `9-9-9`.
 */
class Lotto123 : Lottery(SIZE) {
    override val minimum = 0
    override val maximum = 9
    override val bonusMinimum = 0
    override val bonusMaximum = 0

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

    override fun setCandidates(candidates: Collection<Int>?) {
        if (candidates == null) {
            val balls = IntArray(1000) { it }
            super.setCandidates(balls.toList())
            return
        }
        super.setCandidates(candidates)
    }

    override fun guess(): LotteryGuess {
        val bag = createBag()
        val size = this.size
        val balls = IntArray(size)
        val pickIndex = rnd.nextInt(bag.size)
        var candidate = bag.removeAt(pickIndex)
        balls[0] = candidate % 10
        candidate /= 10
        balls[1] = candidate % 10
        candidate /= 10
        balls[2] = candidate % 10
        return LotteryGuess(balls = balls)
    }

    companion object {
        private const val SIZE = 3

        /**
         * Cost per game.
         */
        const val COST = 1

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
    val numPlays = BUDGET / COST
    val games = lottery.play(numPlays)
    for (game in games) {
        lottery.print(game)
    }
}