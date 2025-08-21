package com.github.pnemonic.game.lottery.pais777

import com.github.pnemonic.game.GameException
import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.game.lottery.LotteryGuess
import com.github.pnemonic.game.lottery.pais777.Lotto777.Companion.COST
import com.github.pnemonic.game.lottery.pais777.Lotto777Tester.Companion.BUDGET
import com.github.pnemonic.isEven

/**
 * Choose numbers for 777 in Israel.
 */
open class Lotto777 : Lottery(SIZE) {
    override val minimum: Int = 1
    override val maximum: Int = 70
    override val bonusMinimum: Int = 0
    override val bonusMaximum: Int = 0

    @Throws(GameException::class)
    override fun filterGame(game: LotteryGame) {
        super.filterGame(game)
        var candidate: Int
        val lot = game.balls
        val size = lot.size

        // Rule: Minimum-valued ball cannot exceed MAX_LOWER.
        candidate = lot[0]
        if (candidate > MAX_LOWER) {
            throw GameException("Minimum-valued ball $candidate exceeds $MAX_LOWER")
        }
        // Rule: Maximum-valued ball cannot precede MIN_UPPER.
        candidate = lot[size - 1]
        if (candidate < MIN_UPPER) {
            throw GameException("Maximum-valued ball $candidate less than $MIN_UPPER")
        }

        // Rule: Minimum number of odd-numbered balls.
        var countOdd = 0
        var countEven = 0
        for (l in lot) {
            if (l.isEven) {
                countEven++
            } else {
                countOdd++
            }
        }
        if (countOdd < MIN_ODD) {
            throw GameException("Odds less than $MIN_ODD")
        }
        if (countEven < MIN_EVEN) {
            throw GameException("Evens less than $MIN_EVEN")
        }
    }

    override fun calculatePrizes(guess: LotteryGuess, result: LotteryGame) {
        var match = 0
        for (ball in guess.balls) {
            if (result.balls.binarySearch(ball) >= 0) {
                match++
            }
        }

        when (match) {
            3 -> result.prize = PRIZE_3
            4 -> result.prize = PRIZE_4
            5 -> result.prize = PRIZE_5
            6 -> result.prize = PRIZE_6
            7 -> result.prize = PRIZE_7
            else -> result.prize = 0
        }
    }

    companion object {
        private const val SIZE = 7
        private const val MAX_LOWER = 23
        private const val MIN_UPPER = 43
        private const val MIN_ODD = 2
        private const val MIN_EVEN = 2

        /**
         * Cost per game.
         */
        const val COST = 7

        /** Prize for guessing 3 correct numbers. */
        const val PRIZE_3 = 5

        /** Prize for guessing 4 correct numbers. */
        const val PRIZE_4 = 20

        /** Prize for guessing 5 correct numbers. */
        const val PRIZE_5 = 50

        /** Prize for guessing 6 correct numbers. */
        const val PRIZE_6 = 500

        /** Prize for guessing all 7 correct numbers. */
        const val PRIZE_7 = 70_000
    }
}

/**
 * Main method.
 *
 * @param args the array of arguments.
 */
fun main(args: Array<String>) {
    //TODO parse history then predict some guesses
    val lottery: Lottery = Lotto777()
    if (args.isNotEmpty()) {
        lottery.setCandidates(args[0])
    }
    val numPlays = BUDGET / COST
    val games = lottery.play(numPlays)
    for (game in games) {
        lottery.print(game)
    }
}