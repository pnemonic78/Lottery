package com.github.pnemonic.game.lottery

import java.util.Arrays
import java.util.TreeSet
import kotlin.random.Random

/**
 * Lottery.
 *
 * @author Moshe
 */
abstract class Lottery(val size: Int) {
    private val candidates: MutableSet<Int> = TreeSet()

    /**
     * Constructs a new lottery.
     */
    init {
        setCandidates(null as IntArray?)
    }

    abstract val minimum: Int

    abstract val maximum: Int

    abstract val bonusMinimum: Int

    abstract val bonusMaximum: Int

    val numberBalls: Int
        get() = maximum - minimum + 1

    /**
     * Choose some numbers.
     *
     * Populate the bag of numbers to choose from, and remove 6 candidates.
     *
     * @return the lot of chosen numbers - <tt>null</tt> if game is invalid.
     */
    fun play(): LotteryGame {
        val size = this.size
        val bag = createBag()
        val game = LotteryGame(size)
        var index: Int
        var candidate: Int
        for (pick in 0 until size) {
            if (bag.isEmpty()) {
                break
            }
            index = rnd.nextInt(bag.size)
            candidate = bag.removeAt(index)
            game.lot[pick] = candidate
            Arrays.sort(game.lot, 0, pick + 1)
            filter(game, pick, bag)
        }
        playBonus(game)
        filterGame(game)
        return game
    }

    /**
     * Print the chosen numbers.
     *
     * @param game the game.
     */
    fun print(game: LotteryGame) {
        print("Play: " + game.id)
        print("\tLotto:")
        for (i in game.lot) {
            print("\t" + i)
        }
        if (game.bonus > 0) {
            print("\tBonus: " + game.bonus)
        }
        println()
    }

    /**
     * Filter the bag by applying various rules of probability.
     *
     * @param game      the game.
     * @param pickIndex the number index.
     * @param bag       the bag of candidate numbers.
     */
    protected open fun filter(game: LotteryGame, pickIndex: Int, bag: MutableList<Int>) {
    }

    /**
     * Filter the game by applying various rules of probability.
     *
     * @param game the game.
     * @throws LotException if the game's lot is invalid.
     */
    @Throws(LotException::class)
    protected open fun filterGame(game: LotteryGame) {
    }

    fun play(numGames: Int): MutableSet<LotteryGame> {
        require(numGames > 0) { "Invalid number of games $numGames" }
        val games: MutableSet<LotteryGame> = TreeSet()
        var game: LotteryGame
        var play = 1
        var retry = 0
        while (games.size < numGames) {
            try {
                game = play()
                game.id = play++
                games.add(game)
            } catch (le: LotException) {
                // TODO System.err.println(le.getMessage());
                retry++
                if (retry >= RETRIES) {
                    break
                }
            }
        }
        play = 1
        for (g in games) {
            g.id = play++
        }
        return games
    }

    protected fun createBag(): MutableList<Int> {
        return ArrayList(candidates)
    }

    fun setCandidates(candidates: IntArray?) {
        this.candidates.clear()
        if (candidates == null || candidates.size < size) {
            val min = minimum
            val max = maximum
            for (n in min..max) {
                this.candidates.add(n)
            }
        } else {
            for (n in candidates) {
                this.candidates.add(n)
            }
        }
    }

    fun setCandidates(candidates: Collection<Int>?) {
        this.candidates.clear()
        if (candidates == null || candidates.size < size) {
            val min = minimum
            val max = maximum
            for (n in min..max) {
                this.candidates.add(n)
            }
        } else {
            this.candidates.addAll(candidates)
        }
    }

    fun setCandidates(candidates: String) {
        val tokens = candidates.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val balls: MutableSet<Int> = TreeSet()
        for (token in tokens) {
            balls.add(Integer.valueOf(token))
        }
        setCandidates(balls)
    }

    protected fun playBonus(game: LotteryGame) {
        if (bonusMinimum < bonusMaximum) {
            val bag = mutableListOf<Int>()
            for (n in bonusMinimum..bonusMaximum) {
                bag.add(n)
            }
            val index = rnd.nextInt(bag.size)
            val candidate = bag.removeAt(index)
            game.bonus = candidate
        } else {
            game.bonus = bonusMinimum
        }
    }

    companion object {
        protected val rnd = Random.Default
        private const val RETRIES = 10
    }
}