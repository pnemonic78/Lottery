package com.github.pnemonic.game.lottery

import com.github.pnemonic.csv.CSVLine
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

abstract class LotteryResultsReader {
    @Throws(IOException::class)
    fun parse(file: File): List<LotteryRecord> {
        val records: List<LotteryRecord>
        FileInputStream(file).use {
            records = parse(it)
        }
        return records
    }

    @Throws(IOException::class)
    abstract fun parse(input: InputStream): List<LotteryRecord>

    protected fun getCleanColumns(line: CSVLine): Array<String> {
        val columns = line.columns
        var col: String
        for (i in columns.indices) {
            col = columns[i]
            if (col.isNotEmpty() && col[0] == '=') {
                col = col.substring(1)
            }
            columns[i] = col
        }
        return columns
    }
}