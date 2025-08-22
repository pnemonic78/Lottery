package com.github.pnemonic

val Int.isEven: Boolean get() = (this and 1 == 0)

fun factorial(n: Int): Long {
    require(n >= 0) { "Factorial is not defined for negative numbers." }
    var result = 1L
    for (i in 2..n) {
        result *= i
    }
    return result
}

fun choose(n: Int, r: Int): Long {
    require(n >= r && r >= 0) { "Sample must be less than items." }
    var result = n.toLong()
    for (i in (n - 1) downTo (n - r + 1)) {
        result *= i
    }
    for (i in 2..r) {
        result /= i
    }
    return result
}

