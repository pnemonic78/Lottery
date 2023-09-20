package com.github.pnemonic.game.lottery

/**
 * Lottery game.
 *
 * @author Moshe
 */
class LotteryGame(size: Int) : Comparable<LotteryGame> {
    @JvmField
    var id = 0
    @JvmField
    var lot: IntArray = IntArray(size)
    @JvmField
    var bonus = 0

    override fun hashCode(): Int {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (other is LotteryGame) {
            return lot.contentEquals(other.lot) && bonus == other.bonus
        }
        return super.equals(other)
    }

    override fun compareTo(other: LotteryGame): Int {
        val size0 = lot.size
        val size1 = other.lot.size
        var c = size0 - size1
        if (c == 0) {
            val lot0 = lot
            val lot1 = other.lot
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

    override fun toString(): String {
        val buf = StringBuilder()
        buf.append(id).append(": ")
        for (ball in lot) {
            buf.append(ball).append(' ')
        }
        buf.append('+').append(bonus)
        return buf.toString()
    }
}