package com.github.pnemonic.game

class CompareByRepetition(descending: Boolean=false) : CompareNumber(descending) {

    override fun compare(o1: NumberStatistic?, o2: NumberStatistic?): Int {
        if ((o1 == null) && (o2 == null)) return 0
        if (o1 == null) return -1
        if (o2 == null) return +1
        var c = if (descending) o2.maxRepeat - o1.maxRepeat else o1.maxRepeat - o2.maxRepeat
        if (c == 0) {
            c = if (descending) o2.repeat - o1.repeat else o1.repeat - o2.repeat
            if (c == 0) {
                c = if (descending) o2.id - o1.id else o1.id - o2.id
            }
        }
        return c
    }
}