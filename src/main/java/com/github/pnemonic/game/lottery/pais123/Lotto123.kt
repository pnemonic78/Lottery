package com.github.pnemonic.game.lottery.pais123

import com.github.pnemonic.game.lottery.Lottery
import kotlin.math.floor

/**
 * Choose numbers for 123 for Israel.
 *
 * @author Moshe
 */
class Lotto123 : Lottery(SIZE) {

    override val minimum: Int = 0

    override val maximum: Int = 9

    override val bonusMinimum: Int = 0

    override val bonusMaximum: Int = 0

    companion object {
        /**
         * Cost per game.
         */
        private const val BUDGET = 10.0

        /**
         * Cost per game.
         */
        private const val COST = 1.0

        /**
         * Total number of plays per budget.
         */
        @JvmField
        val PLAYS = floor(BUDGET / COST).toInt()

        private const val SIZE = 1

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
            val plays = PLAYS
            val games = lottery.play(plays)
            for (game in games) {
                lottery.print(game)
            }
        }
    }
}