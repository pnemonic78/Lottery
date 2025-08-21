package com.github.pnemonic.game.lottery

import com.github.game.GameOfChanceResult
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Lottery game results.
 */
open class LotteryGame(size: Int) : GameOfChanceResult(), Comparable<LotteryGame> {
    var date: Calendar? = null
    var id: Int = 0
    val balls: IntArray = IntArray(size)
    var bonus: Int = 0

    override fun toString(): String {
        val buf = StringBuilder()
        date?.let { buf.append(format.format(it.time)).append(' ') }
        buf.append(id).append(": ")
        for (ball in balls) {
            buf.append(ball).append(' ')
        }
        buf.append('+').append(bonus)
        return buf.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LotteryGame) return false
        return (this.balls contentEquals other.balls) && (bonus == other.bonus)
    }

    override fun hashCode(): Int {
        return id
    }

    override fun compareTo(other: LotteryGame): Int {
        val size0 = balls.size
        val size1 = other.balls.size
        var c = size0 - size1
        if (c == 0) {
            val lot0 = balls
            val lot1 = other.balls
            var i = 0
            while (i < size0 && c == 0) {
                c = lot0[i] - lot1[i]
                i++
            }
            if (c == 0) {
                c = bonus - other.bonus
                if (c == 0) {
                    c = id - other.id
                }
            }
        }
        return c
    }

    fun copy(): LotteryGame {
        val record = this
        return LotteryGame(record.balls.size).apply {
            this.date = record.date
            this.id =  record.id
            record.balls.copyInto(this.balls)
            this.bonus = record.bonus
        }
    }

    companion object {
        private val format: DateFormat = SimpleDateFormat()
    }
}