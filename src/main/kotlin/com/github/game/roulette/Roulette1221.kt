package com.github.game.roulette

import kotlin.math.max

class Roulette1221 : RouletteGame() {
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
            dozen1 = dozen12
            dozen2 = dozen36
        } else {
            lossCount++
            lossCountMax = max(lossCountMax, lossCount)
            bet *= 3

            if (ball != 0) {
                when (lossCount % 5) {
                    1 -> {
                        dozen1 = dozen24
                        dozen2 = dozen36
                    }

                    2 -> {
                        dozen1 = dozen24
                        dozen2 = dozen36
                    }

                    3 -> {
                        dozen1 = dozen12
                        dozen2 = dozen24
                    }

                    4 -> {
                        dozen1 = dozen12
                        dozen2 = dozen24
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

    override fun getStatistics(): RouletteStats = stats
}