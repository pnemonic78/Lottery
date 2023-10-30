package com.github.game.roulette

import kotlin.random.Random

class RouletteLosses {
    fun run() {
        val game1 = Roulette1()
        val game1221 = Roulette1221()
        val game222 = Roulette222()
        val game111 = Roulette111()
        val rnd = Random.Default

        for (i in 1..1000) {
            val ball = rnd.nextInt(0, 37)
            game1.play(ball)
            game1221.play(ball)
            game222.play(ball)
            game111.play(ball)
        }

        val stats1 = game1.getStatistics()
        println("1:")
        println("grouped losses: ${stats1.sequenceLosses.contentToString()}")
        println("max loss sequence: ${stats1.maxSequenceLosses}")
        println("profit: ${stats1.profit}")

        val stats1221 = game1221.getStatistics()
        println("1221:")
        println("grouped losses: ${stats1221.sequenceLosses.contentToString()}")
        println("max loss sequence: ${stats1221.maxSequenceLosses}")
        println("profit: ${stats1221.profit}")

        val stats222 = game222.getStatistics()
        println("222:")
        println("grouped losses: ${stats222.sequenceLosses.contentToString()}")
        println("max loss sequence: ${stats222.maxSequenceLosses}")
        println("profit: ${stats222.profit}")

        val stats111 = game111.getStatistics()
        println("111:")
        println("grouped losses: ${stats111.sequenceLosses.contentToString()}")
        println("max loss sequence: ${stats111.maxSequenceLosses}")
        println("profit: ${stats111.profit}")
    }
}

fun main() {
    RouletteLosses().run()
}

