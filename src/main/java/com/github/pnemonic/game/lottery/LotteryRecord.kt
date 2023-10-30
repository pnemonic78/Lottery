package com.github.pnemonic.game.lottery

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar

class LotteryRecord(
    lottery: Lottery?,
    size: Int = lottery?.size ?: 0
) : Comparable<LotteryRecord> {
    var id = 0
    var date: Calendar? = null
    val lot: IntArray = IntArray(size)
    var bonus = 0

    private val bonusMin: Int = lottery?.bonusMinimum ?: 0
    private val bonusMax: Int = lottery?.bonusMaximum ?: 0

    constructor(size: Int) : this(null, size)

    override fun compareTo(other: LotteryRecord): Int {
        return id - other.id
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        val buf = StringBuilder()
        date?.let { buf.append(format.format(it.time)).append(' ') }
        buf.append(id).append(": ")
        for (ball in lot) {
            buf.append(ball).append(' ')
        }
        buf.append('+').append(bonus)
        return buf.toString()
    }

    /**
     * Compare a played game to an actual game. Each ball has weight
     * <tt>100</tt> and a bonus ball has weight <tt>1</tt>.
     *
     * @param game the game.
     * @return the score.
     */
    operator fun compareTo(game: LotteryGame): Int {
        var same = 0
        for (ball in game.lot) {
            if (Arrays.binarySearch(lot, ball) >= 0) {
                same += 100
            }
        }
        if (bonus == game.bonus) {
            if (bonusMin < bonusMax) {
                same++
            }
        }
        return same
    }

    companion object {
        protected val format: DateFormat = SimpleDateFormat()
    }
}