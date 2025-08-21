package com.github.pnemonic

val Int.isEven: Boolean get() = (this and 1 == 0)

fun factorial(n: Int): Long {
    require(n >= 0) { "Factorial is not defined for negative numbers." }
    var result: Long = 1
    for (i in 1..n) {
        result *= i.toLong() // Use Long to handle larger factorial values
    }
    return result
}

fun choose(n: Int, r: Int): Int {
    return ((factorial(n) / factorial(r)) / factorial(n - r)).toInt()
}

