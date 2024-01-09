package com.github.game.roulette

import kotlin.random.Random

class RouletteLosses {
    fun run() {
        val game1 = Roulette1()
        val game1221 = Roulette1221()
        val game222 = Roulette222()
        val game111 = Roulette111()
        val nextLikely = NextLikely()
        val rnd = Random.Default
        val balls = IntArray(37)

        for (i in 1..1000) {
            val ball = rnd.nextInt(0, 37)
            balls[ball]++
            game1.play(ball)
            game1221.play(ball)
            game222.play(ball)
            game111.play(ball)
            nextLikely.play(ball)
        }

        println("balls: ${balls.mapIndexed { ball, count -> "$ball: $count" }}")

        val stats1 = game1.getStatistics()
        println()
        println("1:")
        println("grouped losses: ${stats1.sequenceLosses.contentToString()}")
        println("max loss sequence: ${stats1.maxSequenceLosses}")
        println("profit: ${stats1.profit}")

        val stats1221 = game1221.getStatistics()
        println()
        println("1221:")
        println("grouped losses: ${stats1221.sequenceLosses.contentToString()}")
        println("max loss sequence: ${stats1221.maxSequenceLosses}")
        println("profit: ${stats1221.profit}")

        val stats222 = game222.getStatistics()
        println()
        println("222:")
        println("grouped losses: ${stats222.sequenceLosses.contentToString()}")
        println("max loss sequence: ${stats222.maxSequenceLosses}")
        println("profit: ${stats222.profit}")

        val stats111 = game111.getStatistics()
        println()
        println("111:")
        println("grouped losses: ${stats111.sequenceLosses.contentToString()}")
        println("max loss sequence: ${stats111.maxSequenceLosses}")
        println("profit: ${stats111.profit}")

        println()
        println("next likely:")
        nextLikely.printStats(System.out)
        val statsNextLikely = nextLikely.getStatistics()
        println("grouped losses: ${statsNextLikely.sequenceLosses.contentToString()}")
        println("max loss sequence: ${statsNextLikely.maxSequenceLosses}")
        println("profit: ${statsNextLikely.profit}")
    }
}

fun main() {
    RouletteLosses().run()
}

