package com.github.pnemonic.game

enum class NumberStatisticGrouping(val description: String?) {
    /**
     * Just regular randomness.
     */
    REGULAR("Regular"),

    /**
     * Most count.
     */
    MOST_COUNT("Most count"),

    /**
     * Least count.
     */
    LEAST_COUNT("Least count"),

    /**
     * Most repeated.
     */
    MOST_REPEAT("Most repeated"),

    /**
     * Least repeated.
     */
    LEAST_REPEAT("Least repeated"),

    /**
     * Most used.
     */
    MOST_USED("Most used"),

    /**
     * Least used.
     */
    LEAST_USED("Least used"),

    MC_LC("MC or LC"),

    LU_LC("LU or LC"),

    LU_MC("LU or MC"),

    LU_MC_LC("LU or MC or LC"),

    MU_LC("MU or LC"),

    MU_MC("MU or MC"),

    MU_MC_LC("MU or MC or LC"),

    MU_LU("MU or LU"),

    MU_LU_LC("MU or LU or LC"),

    MU_LU_MC("MU or LU or MC"),

    MU_LU_MC_LC("MU or LU or MC or LC"),

    LC_AND_LU("LC and LU"),

    LC_AND_MU("LC and MU"),

    MC_AND_LU("MC and LU"),

    MC_AND_MU("MC and MU");

    override fun toString(): String {
        return description ?: name
    }
}