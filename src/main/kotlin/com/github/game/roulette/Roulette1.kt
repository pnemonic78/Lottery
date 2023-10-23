package com.github.game.roulette

import kotlin.math.max

class Roulette1 : RouletteGame() {
    private val stats = RouletteStats()

    private var lossCount = 0
    private var lossCountMax = 0
    private var dozen1: IntRange = dozen12
    private var dozen2: IntRange = dozen36
    private val lossesGrouped = IntArray(15)
    private var wallet = 0
    private var bet = 1

    override fun play(ball: Int) {
        wallet -= (bet * 2)
        val win = (ball in dozen1) || (ball in dozen2)
        if (win) {
            lossesGrouped[lossCount]++
            lossCount = 0
            wallet += (bet * 3)
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