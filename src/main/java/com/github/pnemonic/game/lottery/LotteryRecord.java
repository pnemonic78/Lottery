package com.github.pnemonic.game.lottery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class LotteryRecord implements Comparable<LotteryRecord> {

    protected static final DateFormat format = new SimpleDateFormat();

    protected Lottery lottery;
    public int id;
    public Calendar date;
    public final int[] lot;
    public int bonus;
    private final int bonusMin;
    private final int bonusMax;

    public LotteryRecord(int size) {
        this(null, size);
    }

    public LotteryRecord(Lottery lottery) {
        this(lottery, lottery.size());
    }

    public LotteryRecord(Lottery lottery, int size) {
        super();
        this.lottery = lottery;
        this.lot = new int[size];
        this.bonusMin = (lottery == null) ? 0 : lottery.getBonusMinimum();
        this.bonusMax = (lottery == null) ? 0 : lottery.getBonusMaximum();
    }

    public int compareTo(LotteryRecord that) {
        return this.id - that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(format.format(date.getTime())).append(' ');
        buf.append(id).append(": ");
        for (int ball : lot) {
            buf.append(ball).append(' ');
        }
        buf.append('+').append(bonus);
        return buf.toString();
    }

    /**
     * Compare a played game to an actual game. Each ball has weight
     * <tt>100</tt> and a bonus ball has weight <tt>1</tt>.
     *
     * @param game the game.
     * @return the score.
     */
    public int compareTo(LotteryGame game) {
        int same = 0;
        for (int ball : game.lot) {
            if (Arrays.binarySearch(this.lot, ball) >= 0) {
                same += 100;
            }
        }
        if (this.bonus == game.bonus) {
            if ((lottery == null) || (bonusMin < bonusMax)) {
                same++;
            }
        }

        return same;
    }
}
