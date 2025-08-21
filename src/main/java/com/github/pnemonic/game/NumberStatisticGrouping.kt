package com.github.pnemonic.game

enum class NumberStatisticGrouping(val description: String?) {
    /**
     * Just regular randomness.
     */
    REGULAR("Regular"),

    /**
     * Most repeated.
     */
    MOST_REPEAT("Most repeated"),

    /**
     * Least repeated.
     */
    LEAST_REPEAT("Least repeated"),

    /**
     * Most count.
     */
    MOST_COUNT("Most count."),

    /**
     * Least count.
     */
    LEAST_COUNT("Least count."),

    /**
     * TODO comment me!
     */
    MC_LC(null),

    /**
     * Least used.
     */
    LEAST_USED("Least used."),

    /**
     * TODO comment me!
     */
    LU_LC(null),

    /**
     * TODO comment me!
     */
    LU_MC(null),

    /**
     * TODO comment me!
     */
    LU_MC_LC(null),

    /**
     * Most used.
     */
    MOST_USED("Most used."),

    /**
     * TODO comment me!
     */
    MU_LC(null),

    /**
     * TODO comment me!
     */
    MU_MC(null),

    /**
     * TODO comment me!
     */
    MU_MC_LC(null),

    /**
     * TODO comment me!
     */
    MU_LU(null),

    /**
     * TODO comment me!
     */
    MU_LU_LC(null),

    /**
     * TODO comment me!
     */
    MU_LU_MC(null),

    /**
     * TODO comment me!
     */
    MU_LU_MC_LC(null),

    /**
     * TODO comment me!
     */
    LC_AND_LU(null),

    /**
     * TODO comment me!
     */
    LC_AND_MU(null),

    /**
     * TODO comment me!
     */
    MC_AND_LU(null),

    /**
     * TODO comment me!
     */
    MC_AND_MU(null);

    override fun toString(): String {
        return description ?: name
    }
}