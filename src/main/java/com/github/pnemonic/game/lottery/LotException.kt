package com.github.pnemonic.game.lottery;

/**
 * Error with the lottery lot.
 *
 * @author moshew
 */
public class LotException extends IndexOutOfBoundsException {

    /**
     * Creates a new exception.
     */
    public LotException() {
        super();
    }

    /**
     * Creates a new exception.
     *
     * @param message the detail message.
     */
    public LotException(String message) {
        super(message);
    }

}
