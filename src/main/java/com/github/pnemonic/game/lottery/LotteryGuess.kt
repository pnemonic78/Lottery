package com.github.pnemonic.game.lottery

import com.github.game.GameOfChanceGuess

class LotteryGuess(
    val balls: IntArray
) : GameOfChanceGuess() {
    override fun toString(): String {
        return balls.contentToString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LotteryGuess) return false
        return this.balls contentEquals other.balls
    }

    override fun hashCode(): Int {
        return balls.contentHashCode()
    }
}