package com.github.pnemonic.game.lottery

import com.github.game.GameOfChance
import com.github.pnemonic.choose
import com.github.pnemonic.game.GameException
import kotlin.math.min
import kotlin.random.Random

/**
 * Lottery.
 */
abstract class Lottery(val size: Int) : GameOfChance<LotteryGuess, LotteryGame> {
    private val candidates = mutableSetOf<Int>()

    abstract val minimum: Int

    abstract val maximum: Int

    abstract val bonusMinimum: Int

    abstract val bonusMaximum: Int

    /**
     * The number of possible balls.
     */
    val numberBalls: Int
        get() = maximum - minimum + 1

    init {
        setCandidates(emptyList<Int>())
    }

    /**
     * Choose some numbers.
     *
     * Populate the bag of numbers to choose from, and remove 6 candidates.
     *
     * @return the lot of chosen numbers.
     */
    fun play(): LotteryGame {
        return play(guess())
    }

    override fun play(guess: LotteryGuess): LotteryGame {
        val game = LotteryGame(size)
        game.guess = guess
        playBonus(game)
        filterGame(game)
        calculatePrizes(guess, game)
        return game
    }

    override fun play(guess: LotteryGuess, result: LotteryGame) {
        result.guess = guess
        calculatePrizes(guess, result)
    }

    open protected fun calculatePrizes(guess: LotteryGuess, result: LotteryGame) {
        result.prize = 0
    }

    /**
     * Print the chosen numbers.
     *
     * @param game the game.
     */
    fun print(game: LotteryGame) {
        print("Play: ${game.id}")
        print("\tLotto:")
        for (b in game.balls) {
            print("\t$b")
        }
        if (game.bonus > 0) {
            print("\tBonus: ${game.bonus}")
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
     * @throws com.github.pnemonic.game.GameException if the game's lot is invalid.
     */
    @Throws(GameException::class)
    protected open fun filterGame(game: LotteryGame) {
    }

    fun play(numGames: Int, record: LotteryGame): List<LotteryGame> {
        require(numGames > 0) { "Invalid number of games $numGames" }
        val maxPossibleGuesses = choose(candidates.size, size).toInt()
        val maxNumGames = min(numGames, maxPossibleGuesses)
        val guesses = mutableSetOf<LotteryGuess>()
        val games = mutableListOf<LotteryGame>()
        var retry = 0
        while (games.size < maxNumGames) {
            try {
                val guess = guess()
                if (guesses.contains(guess)) {
                    continue
                }
                guesses.add(guess)
                val game = record.copy()
                play(guess, game)
                games.add(game)
            } catch (e: GameException) {
                // TODO System.err.println(e.getMessage());
                retry++
                if (retry >= RETRIES) {
                    break
                }
            }
        }
        return games.toList()
    }

    fun play(numGames: Int): List<LotteryGame> {
        require(numGames > 0) { "Invalid number of games $numGames" }
        val games = mutableListOf<LotteryGame>()
        var id = 1
        var retry = 0
        while (games.size < numGames) {
            try {
                val game = play()
                game.id = id++
                games.add(game)
            } catch (e: GameException) {
                // TODO System.err.println(e.getMessage());
                retry++
                if (retry >= RETRIES) {
                    break
                }
            }
        }
        return games
    }

    /**
     * Create a bag of balls to choose from.
     */
    protected fun createBag(): MutableList<Int> {
        return ArrayList(candidates)
    }

    override fun guess(): LotteryGuess {
        val bag = createBag()
        val size = this.size
        val balls = IntArray(size)
        for (i in 0 until size) {
            if (bag.isEmpty()) {
                break
            }
            val pickIndex = rnd.nextInt(bag.size)
            balls[i] = bag.removeAt(pickIndex)
            //filter(balls, i)
        }
        balls.sort()
        return LotteryGuess(balls = balls)
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
        val tokens = candidates.split(",".toRegex())
            .dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val balls = mutableSetOf<Int>()
        for (token in tokens) {
            balls.add(token.toInt())
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