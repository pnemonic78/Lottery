package com.github.pnemonic.game

class CompareById(descending: Boolean = false) : CompareNumber(descending) {

    override fun compare(o1: NumberStatistic?, o2: NumberStatistic?): Int {
        if ((o1 == null) && (o2 == null)) return 0
        if (o1 == null) return -1
        if (o2 == null) return +1
        val c = o1.id.compareTo(o2.id)
        return if (descending) -c else c
    }
}