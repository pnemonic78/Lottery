package com.github.pnemonic.game.lottery.pais777

import com.github.pnemonic.game.lottery.LotException
import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryGame
import kotlin.math.floor

/**
 * Choose numbers for 777 for Israel.
 *
 * @author Moshe
 */
open class Lotto777 : Lottery(SIZE) {
    override val minimum: Int = 1
    override val maximum: Int = 70
    override val bonusMinimum: Int = 0
    override val bonusMaximum: Int = 0

    @Throws(LotException::class)
    override fun filter(game: LotteryGame, pickIndex: Int, bag: MutableList<Int>) {
        super.filter(game, pickIndex, bag)

        // Rule: Too few "5 consecutive" numbers.
        if (pickIndex >= 4) {
            val lot = game.lot
            var candidate: Int
            if (lot[pickIndex - 4] + 1 == lot[pickIndex - 3] && lot[pickIndex - 3] + 1 == lot[pickIndex - 2] && lot[pickIndex - 2] + 1 == lot[pickIndex - 1] && lot[pickIndex - 1] + 1 == lot[pickIndex]) {
                // remove 5th consecutive numbers: 1 before; 1 after.
                candidate = lot[pickIndex - 4] - 1
                bag.remove(candidate as Any)
                candidate = lot[pickIndex] + 1
                bag.remove(candidate as Any)
            }
        }
    }

    @Throws(LotException::class)
    override fun filterGame(game: LotteryGame) {
        super.filterGame(game)
        var candidate: Int
        val lot = game.lot
        val size = lot.size

        // Rule: Minimum-valued ball cannot exceed MAX_LOWER.
        candidate = lot[0]
        if (candidate > MAX_LOWER) {
            throw LotException("Minimum-valued ball $candidate exceeds $MAX_LOWER")
        }
        // Rule: Maximum-valued ball cannot precede MIN_UPPER.
        candidate = lot[size - 1]
        if (candidate < MIN_UPPER) {
            throw LotException("Maximum-valued ball $candidate less than $MIN_UPPER")
        }

        // Rule: Biggest gap between 2 balls.
        var l = 0
        for (l1 in 1 until size) {
            if (lot[l] + MAX_GAP <= lot[l1]) {
                throw LotException("Widest gap between " + lot[l] + " and " + lot[l1] + " exceeds " + MAX_GAP)
            }
            l++
        }

        // Rule: Minimum number of odd-numbered balls.
        var countOdd = 0
        var countEven = 0
        for (l in lot) {
            if (l and 1 == 0) {
                countEven++
            } else {
                countOdd++
            }
        }
        if (countOdd < MIN_ODD) {
            throw LotException("Minimum odds less than $MIN_ODD")
        }
        if (countEven < MIN_EVEN) {
            throw LotException("Minimum evens less than $MIN_EVEN")
        }
    }

    companion object {
        /**
         * Number of groupings to play.
         */
        private const val GROUPINGS = 1

        /**
         * Cost per game.
         */
        private const val COST = 7.00

        /**
         * Budget.
         */
        private const val BUDGET = 200.00 / GROUPINGS

        /**
         * Total number of plays per budget.
         */
        val PLAYS = floor(BUDGET / COST).toInt()
        private const val SIZE = 7
        private const val MAX_LOWER = 23
        private const val MIN_UPPER = 43
        private const val MAX_GAP = 32
        private const val MIN_ODD = 2
        private const val MIN_EVEN = 2

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
            val games: Set<LotteryGame> = lottery.play(PLAYS)
            for (game in games) {
                lottery.print(game)
            }
        }
    }
}