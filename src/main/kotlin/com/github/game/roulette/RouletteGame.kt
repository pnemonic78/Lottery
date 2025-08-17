package com.github.game.roulette

import com.github.game.GameOfChance
import com.github.pnemonic.isEven
import kotlin.random.Random

abstract class RouletteGame : GameOfChance<RouletteGuess, RouletteResult> {

    protected val stats = RouletteStats()

    override fun play(guess: RouletteGuess): RouletteResult {
        val ball = Random.Default.nextInt(0, 37)
        val result = RouletteResult(ball)
        play(guess, result)
        return result
    }

    override fun play(guess: RouletteGuess, result: RouletteResult) {
        val ball = result.ball
        if (guess.singles != null) result.prize += guess.singles[ball] * 36
        if (guess.isBlack != null && isBlack(ball)) result.prize += guess.isBlack * 2
        if (guess.isRed != null && isRed(ball)) result.prize += guess.isRed * 2
        if (guess.isEven != null && ball.isEven) result.prize += guess.isEven * 2
        if (guess.isOdd != null && !ball.isEven) result.prize += guess.isOdd * 2
        if (guess.is1To18 != null && (ball in 1..18)) result.prize += guess.is1To18 * 2
        if (guess.is19To36 != null && (ball in 19..36)) result.prize += guess.is19To36 * 2
        if (guess.dozens1 != null && (ball in 1..12)) result.prize += guess.dozens1 * 3
        if (guess.dozens2 != null && (ball in 13..24)) result.prize += guess.dozens2 * 3
        if (guess.dozens3 != null && (ball in 25..36)) result.prize += guess.dozens3 * 3
        if (guess.column1 != null && (ball % 3 == 1)) result.prize += guess.column1 * 3
        if (guess.column2 != null && (ball % 3 == 2)) result.prize += guess.column2 * 3
        if (guess.column3 != null && (ball > 0) && (ball % 3 == 0)) result.prize += guess.column3 * 3
        if (guess.line1To6 != null && (ball in Roulette5.line1To6)) result.prize += guess.line1To6 * 6
        if (guess.line4To9 != null && (ball in Roulette5.line4To9)) result.prize += guess.line4To9 * 6
        if (guess.line7To12 != null && (ball in Roulette5.line7To12)) result.prize += guess.line7To12 * 6
        if (guess.line10To15 != null && (ball in Roulette5.line10To15)) result.prize += guess.line10To15 * 6
        if (guess.line13To18 != null && (ball in Roulette5.line13To18)) result.prize += guess.line13To18 * 6
        if (guess.line16To21 != null && (ball in Roulette5.line16To21)) result.prize += guess.line16To21 * 6
        if (guess.line19To24 != null && (ball in Roulette5.line19To24)) result.prize += guess.line19To24 * 6
        if (guess.line22To27 != null && (ball in Roulette5.line22To27)) result.prize += guess.line22To27 * 6
        if (guess.line25To30 != null && (ball in Roulette5.line25To30)) result.prize += guess.line25To30 * 6
        if (guess.line28To33 != null && (ball in Roulette5.line28To33)) result.prize += guess.line28To33 * 6
        if (guess.line31To36 != null && (ball in Roulette5.line31To36)) result.prize += guess.line31To36 * 6
        //TODO check other prizes
    }

    abstract fun play(ball: Int)

    abstract fun getStatistics(): RouletteStats

    private fun isRed(ball: Int): Boolean {
        return when (ball) {
            1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36 -> true
            else -> false
        }
    }

    private fun isBlack(ball: Int): Boolean {
        return when (ball) {
            2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35 -> true
            else -> false
        }
    }

    companion object {
        val rouletteRange = 0..36
        val dozen12 = 1..12
        val dozen24 = 13..24
        val dozen36 = 25..36
        val dozens = mapOf(
            1 to dozen12,
            2 to dozen24,
            3 to dozen36
        )
    }
}