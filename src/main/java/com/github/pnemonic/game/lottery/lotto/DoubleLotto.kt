package com.github.pnemonic.game.lottery.lotto

import com.github.pnemonic.game.lottery.LotteryGame
import kotlin.math.floor

/**
 * Choose numbers for Double Lotto for Israel.
 */
class DoubleLotto : Lotto() {
}

/**
 * Main method.
 *
 * @param args the array of arguments.
 */
fun main() {
    val lotto = DoubleLotto()
    val budget = 100.00
    val cost = Lotto.COST * 2 // Double Lotto costs 2x
    var plays = floor(budget / cost).toInt()
    plays = plays and 1.inv() // Lotto played in pairs.
    val games: Set<LotteryGame> = lotto.play(plays)
    for (game in games) {
        lotto.print(game)
    }
}
