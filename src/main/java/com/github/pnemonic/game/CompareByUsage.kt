package com.github.pnemonic.game

class CompareByUsage(descending: Boolean = false) : CompareNumber(descending) {

    override fun compare(o1: NumberStatistic?, o2: NumberStatistic?): Int {
        if ((o1 == null) && (o2 == null)) return 0
        if (o1 == null) return -1
        if (o2 == null) return +1
        var c = ((if (descending) o2.usage - o1.usage else o1.usage - o2.usage) * 1e+9).toInt()
        if (c == 0) {
            c = if (descending) o2.id - o1.id else o1.id - o2.id
        }
        return c
    }
}