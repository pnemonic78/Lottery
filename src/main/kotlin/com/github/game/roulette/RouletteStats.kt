package com.github.game.roulette

class RouletteStats {
    // maximum sequence of losses
    var maxSequenceLosses = 0
    // count of each sequence of losses
    var sequenceLosses = IntArray(20)
    // net profit
    var profit = 0
}