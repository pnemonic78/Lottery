package com.github.pnemonic.game;

public class CompareByRepetition extends CompareNumber {

    public CompareByRepetition() {
        super();
    }

    public CompareByRepetition(boolean descending) {
        super(descending);
    }

    public int compare(NumberStatistic o1, NumberStatistic o2) {
        int comp = descending ? (o2.maxRepeat - o1.maxRepeat) : (o1.maxRepeat - o2.maxRepeat);
        if (comp == 0) {
            comp = descending ? (o2.repeat - o1.repeat) : (o1.repeat - o2.repeat);
            if (comp == 0) {
                comp = descending ? (o2.id - o1.id) : (o1.id - o2.id);
            }
        }
        return comp;
    }

}
