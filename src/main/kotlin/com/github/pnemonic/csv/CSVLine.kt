package com.github.pnemonic.csv

data class CSVLine(val columns: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CSVLine) return false
        return columns contentEquals other.columns
    }

    override fun hashCode(): Int {
        return columns.contentHashCode()
    }
}
