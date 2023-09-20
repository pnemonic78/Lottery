package com.github.pnemonic.game

import com.github.pnemonic.game.lottery.LotteryRecord

class RecordStatistic(size: Int) : Statistic() {
    @JvmField
    var record: LotteryRecord? = null

    /**
     * Number of <tt>index + 2</tt> consecutives.
     */
    @JvmField
    val consecutive: IntArray

    /**
     * Gap between ball at <tt>index</tt> and ball at <tt>index + 1</tt>.
     */
    @JvmField
    val gap: IntArray

    /**
     * Number of odd-numbered balls.
     */
    @JvmField
    var odd = 0

    /**
     * Number of even-numbered balls.
     */
    @JvmField
    var even = 0

    init {
        consecutive = IntArray(size)
        gap = IntArray(size)
    }
}