package com.github.game.roulette

import com.github.game.GameOfChanceGuess

class RouletteGuess(
    /** Single number bet: pays 35 to 1. Also called “straight up.” */
    val singles: IntArray,
    /** Double number bet: pays 17 to 1. Also called a “split.” */
    val doubles: IntArray? = null,
    /** Three number bet: pays 11 to 1. Also called a “street.” */
    val threes: IntArray? = null,
    /** Four number bet: pays 8 to 1. Also called a “corner bet.” */
    val fours: IntArray? = null,
    /** Five number bet: pays 6 to 1. Only one specific bet which includes the following numbers: 0-00-1-2-3. */
    val fives: IntArray? = null,
    /** Six number bets: pays 5 to 1. Example: 7, 8, 9, 10, 11, 12. Also called a “line.” */
    val sixes: IntArray? = null,
    /** Twelve numbers or dozens: (first, second, third dozen) pays 2 to 1. */
    val dozens: IntArray? = null,
    /** Column bet (12 numbers in a row): pays 2 to 1. */
    val columns: IntArray? = null,
    /**
     * 18 numbers (1-18): pays even money.
     * 18 numbers (19-36): pays even money.
     */
    val eighteens: IntArray? = null,
    /** Red or black: pays even money. */
    val colors: IntArray? = null,
    /** Odd or even: bets pay even money. */
    val oddOrEven: IntArray? = null
) : GameOfChanceGuess()