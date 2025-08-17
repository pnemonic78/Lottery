package com.github.game.roulette

import kotlin.math.max

class Roulette111 : RouletteGame() {
    private var lossCount = 0
    private var lossCountMax = 0
    private var dozen1: IntRange = dozen12
    private var dozen2: IntRange = dozen36
    private val lossesGrouped = IntArray(15)
    private var wallet = 0
    private var bet = 1

    override fun play(ball: Int) {
        wallet -= bet * 2
        val result = RouletteResult(ball)
        val guess = guess()
        play(guess, result)
        if (result.prize > 0) {
            lossesGrouped[lossCount]++
            lossCount = 0
            wallet += result.prize
            bet = 1
            dozen1 = dozen12
            dozen2 = dozen36
        } else {
            lossCount++
            lossCountMax = max(lossCountMax, lossCount)
            bet *= 3

            if (ball != 0) {
                when (lossCount % 3) {
                    1 -> {
                        dozen1 = dozen12
                        dozen2 = dozen24
                    }

                    2 -> {
                        dozen1 = dozen24
                        dozen2 = dozen36
                    }

                    else -> {
                        dozen1 = dozen12
                        dozen2 = dozen36
                    }
                }
            }
        }

        stats.profit = wallet
        stats.maxSequenceLosses = lossCountMax
        stats.sequenceLosses = lossesGrouped
    }

    override fun guess(): RouletteGuess {
        return RouletteGuess(
            dozens1 = if (dozen1 === dozen12 || dozen2 === dozen12) bet else null,
            dozens2 = if (dozen1 === dozen24 || dozen2 === dozen24) bet else null,
            dozens3 = if (dozen1 === dozen36 || dozen2 === dozen36) bet else null
        )
    }

    override fun getStatistics(): RouletteStats = stats
}