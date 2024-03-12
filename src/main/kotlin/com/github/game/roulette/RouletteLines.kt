package com.github.game.roulette

import kotlin.math.max
import kotlin.math.min

/**
 * Place line bets (like dozens but more granular).
 * Lines:
 * <ol>
 * <li>0
 * <li>1-3
 * <li>4-6
 * <li>7-9
 * <li>10-12
 * <li>13-15
 * <li>16-18
 * <li>19-21
 * <li>22-24
 * <li>25-27
 * <li>28-30
 * <li>31-33
 * <li>34-36
 * </ol>
 */
class Roulette5 : RouletteGame() {
    private val stats = RouletteStats()

    private var lossCount = 0
    private var lossCountMax = 0
    private var line1: IntRange = line1To6
    private var line2: IntRange = line7To12
    private var line3: IntRange = line16To21
    private var line4: IntRange = line25To30
    private var line5: IntRange = line31To36
    private val lossesGrouped = IntArray(15)
    private var wallet = 0
    private var bet = 1

    override fun play(ball: Int) {
        wallet -= bet * 5
        val win = (ball in line1) || (ball in line2) || (ball in line3) || (ball in line4) || (ball in line5)
        if (win) {
            lossesGrouped[lossCount]++
            lossCount = 0
            wallet += bet * 6
            bet = 1
            line3 = line16To21
        } else {
            lossCount++
            lossCountMax = max(lossCountMax, lossCount)
            bet *= 2

            if (ball != 0) {
                when (lossCount % 3) {
                    1 -> {
                        line3 = line19To24
                    }

                    2 -> {
                        line3 = line13To18
                    }

                    else -> {
                        line3 = line16To21
                    }
                }
            }
        }

        stats.profit = wallet
        stats.maxSequenceLosses = lossCountMax
        stats.sequenceLosses = lossesGrouped
    }

    override fun getStatistics(): RouletteStats = stats

    companion object {
        val line1To3 = 1..3
        val line4To6 = 4..6
        val line7To9 = 7..9
        val line10To12 = 10..12
        val line13To15 = 13..15
        val line16To18 = 16..18
        val line19To21 = 19..21
        val line22To24 = 22..24
        val line25To27 = 25..27
        val line28To30 = 28..30
        val line31To33 = 31..33
        val line34To36 = 34..36

        val line1To6 = line1To3 + line4To6
        val line7To12 = line7To9 + line10To12
        val line13To18 = line13To15 + line16To18
        val line16To21 = line16To18 + line19To21
        val line19To24 = line19To21 + line22To24
        val line25To30 = line25To27 + line28To30
        val line31To36 = line31To33 + line34To36
    }
}

operator fun IntRange.plus(other: IntRange): IntRange {
    val start = min(this.first, other.first)
    val endInclusive = max(this.last, other.last)
    return IntRange(start, endInclusive)
}