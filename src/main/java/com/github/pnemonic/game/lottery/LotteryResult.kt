package com.github.pnemonic.game.lottery

import com.github.game.GameOfChanceResult
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

open class LotteryResult(size: Int) : GameOfChanceResult() {
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
        if (other !is LotteryResult) return false
        return this.balls.contentEquals(other.balls) && (bonus == other.bonus)
    }

    override fun hashCode(): Int {
        return id
    }

    companion object {
        private val format: DateFormat = SimpleDateFormat()
    }
}