package com.github.pnemonic.game;


public class CompareByCount extends CompareNumber {

    public CompareByCount() {
        super();
    }

    public CompareByCount(boolean descending) {
        super(descending);
    }

    public int compare(NumberStatistic o1, NumberStatistic o2) {
        int comp = descending ? (o2.count - o1.count) : (o1.count - o2.count);
        if (comp == 0) {
            comp = descending ? (o2.id - o1.id) : (o1.id - o2.id);
        }
        return comp;
    }

}
