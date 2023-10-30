package com.github.pnemonic.game

class NumberStatistic : Statistic(), Comparable<NumberStatistic> {
    /**
     * The ball value.
     */
    var id = 0

    /**
     * Number of occurrences of this ball in the game.
     */
    var occur = 0

    /**
     * Total number of occurrences.
     */
    var count = 0

    /**
     * Usage (age).
     */
    var usage = 0f

    /**
     * Total number of repetitions of consecutive games. Related to [.usage].
     */
    var repeat = 0

    /**
     * Maximum number of repetitions of consecutive games.
     */
    var maxRepeat = 0

    /**
     * Index when sorted by "least count".
     */
    var indexLeastCount = 0

    /**
     * TODO comment me!
     */
    var indexMostCount = 0

    /**
     * TODO comment me!
     */
    var indexLeastUsed = 0

    /**
     * TODO comment me!
     */
    var indexMostUsed = 0

    /**
     * TODO comment me!
     */
    var indexLeastRepeat = 0

    /**
     * TODO comment me!
     */
    var indexMostRepeat = 0

    override fun compareTo(other: NumberStatistic): Int {
        var c = ((usage - other.usage) * 1e+8).toInt()
        if (c == 0) {
            c = id - other.id
        }
        return c
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        val buf = StringBuilder()
        buf.append(id)
        buf.append('\t').append(count)
        buf.append('\t').append(usage)
        buf.append('\t').append(repeat)
        buf.append('\t').append(maxRepeat)
        return buf.toString()
    }
}