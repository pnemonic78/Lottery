package com.github.pnemonic.game.lottery.lotto

import com.github.pnemonic.game.GameException
import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryGame
import kotlin.math.floor

/**
 * Choose numbers for Double Lotto for Israel.
 *
 * @author Moshe
 */
open class Lotto : Lottery(SIZE) {
    override val minimum: Int = 1
    override val maximum: Int = 37
    override val bonusMinimum: Int = 1
    override val bonusMaximum: Int = 8

    @Throws(GameException::class)
    override fun filter(game: LotteryGame, pickIndex: Int, bag: MutableList<Int>) {
        super.filter(game, pickIndex, bag)
        var candidate: Int

        // Rule: Too few "more than triple" consecutive numbers.
        if (pickIndex >= 2) {
            val lot = game.lot
            val size = lot.size

            val i = pickIndex
            if (lot[i - 2] + 1 == lot[i - 1] && lot[i - 1] + 1 == lot[i]) {
                // remove 4th consecutive numbers: 1 before; 1 after.
                candidate = lot[i - 2] - 1
                bag.remove(candidate)
                candidate = lot[i] + 1
                bag.remove(candidate)
            }

            // Rule: No double triples.
            if (pickIndex >= 4) {
                if (lot[0] + 1 == lot[1] && lot[1] + 1 == lot[2] && lot[3] + 1 == lot[4]) {
                    // remove 4th consecutive numbers: 1 before; 1 after.
                    candidate = lot[3] - 1
                    bag.remove(candidate)
                    candidate = lot[4] + 1
                    bag.remove(candidate)
                } else if (lot[0] + 1 == lot[1] && lot[2] + 1 == lot[3] && lot[3] + 1 == lot[4]) {
                    // remove 4th consecutive numbers: 1 before; 1 after.
                    candidate = lot[0] - 1
                    bag.remove(candidate)
                    candidate = lot[2] + 1
                    bag.remove(candidate)
                }
            }

            // Rule: No more than 3 pairs, of which 2 pairs are a triple.
            if (pickIndex >= 3) {
                if (lot[0] + 1 == lot[1] && lot[2] + 1 == lot[3]) {
                    // remove 3rd pair.
                    candidate = lot[4] - 1
                    bag.remove(candidate)
                    candidate = lot[4] + 1
                    bag.remove(candidate)
                } else if (lot[0] + 1 == lot[1] && lot[3] + 1 == lot[4]) {
                    // remove 3rd pair.
                    candidate = lot[2] - 1
                    bag.remove(candidate)
                    candidate = lot[2] + 1
                    bag.remove(candidate)
                } else if (lot[1] + 1 == lot[2] && lot[3] + 1 == lot[4]) {
                    // remove 3rd pair.
                    candidate = lot[1] - 1
                    bag.remove(candidate)
                    candidate = lot[1] + 1
                    bag.remove(candidate)
                }
            }
            if (pickIndex + 1 == SIZE) {
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

                // Rule: Biggest gap between 2 balls.
                var l = 0
                for (l1 in 1 until size) {
                    if (lot[l] + MAX_GAP <= lot[l1]) {
                        throw GameException("Widest gap between " + lot[l] + " and " + lot[l1] + " exceeds " + MAX_GAP)
                    }
                    l++
                }
            }
        }
    }

    companion object {
        /**
         * Cost per game.
         */
        private const val BUDGET = 100.0

        /**
         * Cost per game.
         */
        const val COST = 2.90

        /**
         * Total number of plays per budget. Lotto played in pairs.
         */
        val PLAYS = floor(BUDGET / COST).toInt() and 1.inv()

        private const val SIZE = 6
        private const val MAX_LOWER = 21
        private const val MIN_UPPER = 13
        private const val MAX_GAP = 28
    }
}

/**
 * Main method.
 *
 * @param args the array of arguments.
 */
fun main(args: Array<String>) {
    val lottery: Lottery = Lotto()
    if (args.isNotEmpty()) {
        lottery.setCandidates(args[0])
    }
    val plays = Lotto.PLAYS
    val games: Set<LotteryGame> = lottery.play(plays)
    for (game in games) {
        lottery.print(game)
    }
}