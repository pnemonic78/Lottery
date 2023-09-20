package com.github.pnemonic.game.lottery

/**
 * Error with the lottery lot.
 *
 * @author moshew
 */
class LotException : IndexOutOfBoundsException {
    /**
     * Creates a new exception.
     */
    constructor() : super()

    /**
     * Creates a new exception.
     *
     * @param message the detail message.
     */
    constructor(message: String) : super(message)
}