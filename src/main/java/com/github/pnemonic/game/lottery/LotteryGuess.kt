package com.github.pnemonic.game.lottery

import com.github.game.GameOfChanceGuess

class LotteryGuess(
    val balls: IntArray
): GameOfChanceGuess() {
    override fun toString(): String {
        return balls.contentToString()
    }
}