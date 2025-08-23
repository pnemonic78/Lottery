package com.github.pnemonic.game.lottery

import com.github.pnemonic.csv.CSVLine
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

typealias RecordCallback = (LotteryRecord) -> Unit

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

    @Throws(IOException::class)
    fun parse(file: File, visitor: RecordCallback) {
        FileInputStream(file).use {
            parse(it, visitor)
        }
    }

    @Throws(IOException::class)
    abstract fun parse(input: InputStream, visitor: RecordCallback)
}