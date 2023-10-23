package com.github.game.roulette

abstract class RouletteGame {
    abstract fun play(ball: Int)

    abstract fun getStatistics(): RouletteStats

    companion object {
        val dozen12 = 1..12
        val dozen24 = 13..24
        val dozen36 = 25..36
    }
}