package com.github.game.roulette

abstract class RouletteGame {
    abstract fun play(ball: Int)

    abstract fun getStatistics(): RouletteStats

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