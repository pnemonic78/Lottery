package com.github.game

interface GameOfChance<G : GameOfChanceGuess, R : GameOfChanceResult> {
    fun play(guess: G): R

    /**
     * Play the game with a known result, and accumulate the statistics.
     */
    fun play(guess: G, result: R)

    fun guess(): G
}