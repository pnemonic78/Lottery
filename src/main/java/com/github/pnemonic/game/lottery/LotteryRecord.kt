package com.github.pnemonic.game.lottery

class LotteryRecord(size: Int) : LotteryResult(size), Comparable<LotteryRecord> {

    override fun compareTo(other: LotteryRecord): Int {
        return id - other.id
    }

    /**
     * Compare a played game to an actual game. Each ball has weight
     * <tt>100</tt> and a bonus ball has weight <tt>1</tt>.
     *
     * @param game the game.
     * @return the score.
     */
    operator fun compareTo(game: LotteryGame): Int {
        var same = 0
        for (ball in game.balls) {
            if (balls.binarySearch(ball) >= 0) {
                same += 100
            }
        }
        if ((game.bonus > 0) && bonus == game.bonus) {
            same++
        }
        return same
    }
}