package com.github.pnemonic.csv

data class CSVLine(val columns: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CSVLine

        if (!columns.contentEquals(other.columns)) return false

        return true
    }

    override fun hashCode(): Int {
        return columns.contentHashCode()
    }
}
