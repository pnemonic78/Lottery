package com.zlango.csv

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

    companion object {
        fun parse(line: String): CSVLine {
            // FIXME parse quotes.
            val columns = line.split(",").toTypedArray()
            return CSVLine(columns)
        }
    }
}
