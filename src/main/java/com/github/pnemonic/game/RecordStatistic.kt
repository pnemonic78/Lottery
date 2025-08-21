package com.github.pnemonic.game

import com.github.pnemonic.game.lottery.LotteryRecord

class RecordStatistic(val record: LotteryRecord) : Statistic() {

    /**
     * Number of <tt>index + 2</tt> consecutives.
     */
    val consecutive: IntArray

    /**
     * Gap between ball at <tt>index</tt> and ball at <tt>index + 1</tt>.
     */
    val gap: IntArray

    /**
     * Number of odd-numbered balls.
     */
    var odd = 0

    /**
     * Number of even-numbered balls.
     */
    var even = 0

    init {
        val sizeConsecutives = record.balls.size - 1
        consecutive = IntArray(sizeConsecutives)
        gap = IntArray(sizeConsecutives)
    }
}