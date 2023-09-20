package com.github.pnemonic.game.lottery.pais777

import java.io.File

class Lotto777SubStats {
    companion object {
        fun main(args: Array<String>) {
            val fileName = if (args.isEmpty()) "results/777.csv" else args[0]
            val file = File(fileName)
            val stats = Lotto777Stats(Lotto777Sub())
            stats.parse(file)
        }
    }
}