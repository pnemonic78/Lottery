package com.github.pnemonic.game.lottery

/**
 * Lottery game.
 */
class LotteryGame(size: Int) : LotteryResult(size), Comparable<LotteryGame> {

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
}