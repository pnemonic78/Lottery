package com.github.pnemonic.game

abstract class CompareNumber @JvmOverloads constructor(@JvmField protected val descending: Boolean = false) :
    Comparator<NumberStatistic?>