package com.github.pnemonic

val Int.isEven: Boolean get() = (this and 1 == 0)
