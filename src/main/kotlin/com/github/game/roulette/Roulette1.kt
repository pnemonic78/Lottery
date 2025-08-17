package com.github.game.roulette

import kotlin.math.max

class Roulette1 : RouletteGame() {
    private var lossCount = 0
    private var lossCountMax = 0
    private val lossesGrouped = IntArray(15)
    private var wallet = 0
    private var bet = 1

    override fun play(ball: Int) {
        wallet -= bet * 2
        val result = RouletteResult(ball)
        val guess = RouletteGuess(
            dozens1 = bet,
            dozens3 = bet
        )
        play(guess, result)
        if (result.prize > 0) {
            lossesGrouped[lossCount]++
            lossCount = 0
            wallet += result.prize
            bet = 1
        } else {
            lossCount++
            lossCountMax = max(lossCountMax, lossCount)
            bet *= 3
        }

        stats.profit = wallet
        stats.maxSequenceLosses = lossCountMax
        stats.sequenceLosses = lossesGrouped
    }

    override fun getStatistics(): RouletteStats = stats
}