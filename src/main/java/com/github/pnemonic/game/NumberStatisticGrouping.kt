package com.github.pnemonic.game

enum class NumberStatisticGrouping {
    /**
     * Just regular randomness.
     */
    REGULAR,

    /**
     * Least repeated.
     */
    LEAST_REPEAT,

    /**
     * Most repeated.
     */
    MOST_REPEAT,

    /**
     * Least count.
     */
    LEAST_COUNT,

    /**
     * Most count.
     */
    MOST_COUNT,

    /**
     * TODO comment me!
     */
    MC_LC,

    /**
     * Least used.
     */
    LEAST_USED,

    /**
     * TODO comment me!
     */
    LU_LC,

    /**
     * TODO comment me!
     */
    LU_MC,

    /**
     * TODO comment me!
     */
    LU_MC_LC,

    /**
     * Most used.
     */
    MOST_USED,

    /**
     * TODO comment me!
     */
    MU_LC,

    /**
     * TODO comment me!
     */
    MU_MC,

    /**
     * TODO comment me!
     */
    MU_MC_LC,

    /**
     * TODO comment me!
     */
    MU_LU,

    /**
     * TODO comment me!
     */
    MU_LU_LC,

    /**
     * TODO comment me!
     */
    MU_LU_MC,

    /**
     * TODO comment me!
     */
    MU_LU_MC_LC,

    /**
     * TODO comment me!
     */
    LC_AND_LU,

    /**
     * TODO comment me!
     */
    LC_AND_MU,

    /**
     * TODO comment me!
     */
    MC_AND_LU,

    /**
     * TODO comment me!
     */
    MC_AND_MU
}