package com.github.pnemonic.game.lottery.pais777

import com.github.pnemonic.game.GameException
import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryGame
import com.github.pnemonic.isEven
import kotlin.math.floor

/**
 * Choose numbers for 777 for Israel.
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
        val lot = game.lot
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

    companion object {
        /**
         * Cost per game.
         */
        private const val COST = 7.00

        /**
         * Budget.
         */
        private const val BUDGET = 200.00 / COST

        /**
         * Total number of plays per budget.
         */
        val PLAYS = floor(BUDGET / COST).toInt()

        private const val SIZE = 7
        private const val MAX_LOWER = 23
        private const val MIN_UPPER = 43
        private const val MIN_ODD = 2
        private const val MIN_EVEN = 2

        /** Prize for guessing 3 correct numbers. */
        const val PRIZE_3 = 5.00
        /** Prize for guessing 4 correct numbers. */
        const val PRIZE_4 = 20.00
        /** Prize for guessing 5 correct numbers. */
        const val PRIZE_5 = 50.00
        /** Prize for guessing 6 correct numbers. */
        const val PRIZE_6 = 500.00
        /** Prize for guessing all 7 correct numbers. */
        const val PRIZE_7 = 70_000.00
    }
}

/**
 * Main method.
 *
 * @param args the array of arguments.
 */
fun main(args: Array<String>) {
    val lottery: Lottery = Lotto777()
    if (args.isNotEmpty()) {
        lottery.setCandidates(args[0])
    }
    val games = lottery.play(Lotto777.PLAYS)
    for (game in games) {
        lottery.print(game)
    }
}