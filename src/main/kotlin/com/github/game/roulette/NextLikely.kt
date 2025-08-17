package com.github.game.roulette

import java.io.PrintStream
import kotlin.math.max
import kotlin.math.min

class NextLikely : RouletteGame() {

    // Map<previous, Map<next, count>>
    private val nextCounts = mutableMapOf<Int, MutableMap<Int, Int>>()

    // Map<previous, Map<next_dozen, count>>
    private val nextDozens = mutableMapOf<Int, MutableMap<Int, Int>>()
    private var previous = -1

    private var lossCount = 0
    private var lossCountMax = 0
    private var dozen1: IntRange = dozen12
    private var dozen2: IntRange = dozen36
    private val lossesGrouped = IntArray(15)
    private var wallet = 0
    private var bet = 1

    override fun play(ball: Int) {
        val previous = this.previous
        this.previous = ball
        if (previous < 0) return
        if (ball != 0) {
            var counts: MutableMap<Int, Int>? = nextCounts[previous]
            if (counts == null) {
                counts = mutableMapOf()
                nextCounts[previous] = counts
            }
            counts[ball] = (counts[ball] ?: 0) + 1

            counts = nextDozens[previous]
            if (counts == null) {
                counts = mutableMapOf()
                nextDozens[previous] = counts
            }
            val dozen = when (ball) {
                in dozen12 -> 1
                in dozen24 -> 2
                in dozen36 -> 3
                else -> 0
            }
            counts[dozen] = (counts[dozen] ?: 0) + 1
        }

        wallet -= bet * 2
        val result = RouletteResult(ball)
        val guess = RouletteGuess(
            dozens1 = if (dozen1 == dozen12 || dozen2 == dozen12) bet else null,
            dozens2 = if (dozen1 == dozen24 || dozen2 == dozen24) bet else null,
            dozens3 = if (dozen1 == dozen36 || dozen2 == dozen36) bet else null
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

        when (ball) {
            0 -> {
                dozen1 = dozen24
                dozen2 = dozen36
            }

            1 -> {
                dozen1 = dozen24
                dozen2 = dozen36
            }

            2 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }

            3 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }

            4 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }

            5 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }

            6 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            7 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }

            8 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            9 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }

            10 -> {
                dozen1 = dozen24
                dozen2 = dozen36
            }

            11 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }

            12 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            13 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            14 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }

            15 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            16 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            17 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            18 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            19 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            20 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            21 -> {
                dozen1 = dozen24
                dozen2 = dozen36
            }

            22 -> {
                dozen1 = dozen24
                dozen2 = dozen36
            }

            23 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            24 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            25 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            26 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }

            27 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            28 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }

            29 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            30 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }

            31 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            32 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            33 -> {
                dozen1 = dozen24
                dozen2 = dozen36
            }

            34 -> {
                dozen1 = dozen12
                dozen2 = dozen24
            }

            35 -> {
                dozen1 = dozen24
                dozen2 = dozen36
            }

            36 -> {
                dozen1 = dozen12
                dozen2 = dozen36
            }
        }

        stats.profit = wallet
        stats.maxSequenceLosses = lossCountMax
        stats.sequenceLosses = lossesGrouped
    }

    override fun getStatistics(): RouletteStats = stats

    fun printStats(printer: PrintStream) {
        val comparator = SortByCountDescending()

        for (previous in rouletteRange) {
            val counts = nextCounts[previous] ?: continue
            val sorted = counts.map { Pair(it.key, it.value) }
                .sortedWith(comparator)
                .map { it.first }
                .subList(0, min(12, counts.size))

            val countsDozens = nextDozens[previous] ?: continue
            val sortedDozens = countsDozens.map { Pair(it.key, it.value) }
                .sortedWith(comparator)
                .map { "${dozens[it.first]}: ${it.second}" }

            printer.println("$previous: $sorted $sortedDozens")
        }
    }

    private class SortByCountDescending : Comparator<Pair<Int, Int>> {
        override fun compare(var1: Pair<Int, Int>, var2: Pair<Int, Int>): Int {
            val byCount = var1.second.compareTo(var2.second)
            if (byCount != 0) return -byCount
            return var1.first.compareTo(var2.first)
        }
    }
}