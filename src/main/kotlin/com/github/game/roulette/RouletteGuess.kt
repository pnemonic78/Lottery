package com.github.game.roulette

import com.github.game.GameOfChanceGuess
import com.github.game.roulette.Roulette5.Companion.line16To21
import com.github.game.roulette.Roulette5.Companion.line25To30
import com.github.game.roulette.Roulette5.Companion.line31To36
import com.github.game.roulette.Roulette5.Companion.line7To12

class RouletteGuess(
    /** Single number bet: pays 35 to 1. Also called “straight up.” */
    val singles: IntArray? = null,
    /** Double number bet: pays 17 to 1. Also called a “split.” */
    val doubles: IntArray? = null,
    /** Three number bet: pays 11 to 1. Also called a “street.” */
    val threes: IntArray? = null,
    /** Four number bet: pays 8 to 1. Also called a “corner bet.” */
    val fours: IntArray? = null,
    /** Five number bet: pays 6 to 1. Only one specific bet which includes the following numbers: 0-00-1-2-3. */
    val fives: IntArray? = null,
    /** Six number bets: pays 5 to 1. Example: 7, 8, 9, 10, 11, 12. Also called a “line.” */
    val line1To6: Int? = null,
    val line4To9: Int? = null,
    val line7To12: Int? = null,
    val line10To15: Int? = null,
    val line13To18: Int? = null,
    val line16To21: Int? = null,
    val line19To24: Int? = null,
    val line22To27: Int? = null,
    val line25To30: Int? = null,
    val line28To33: Int? = null,
    val line31To36: Int? = null,
    /** Twelve numbers or dozens: (first, second, third dozen) pays 2 to 1. */
    val dozens1: Int? = null,
    val dozens2: Int? = null,
    val dozens3: Int? = null,
    /** Column bet (12 numbers in a row): pays 2 to 1. */
    val column1: Int? = null,
    val column2: Int? = null,
    val column3: Int? = null,
    /** 18 numbers (1-18): pays even money. */
    val is1To18: Int? = null,
    /** 18 numbers (19-36): pays even money. */
    val is19To36: Int? = null,
    /** Red or black: pays even money. */
    val isRed: Int? = null,
    /** Red or black: pays even money. */
    val isBlack: Int? = null,
    /** Odd or even: bets pay even money. */
    val isOdd: Int? = null,
    /** Odd or even: bets pay even money. */
    val isEven: Int? = null,
) : GameOfChanceGuess()