package com.github.pnemonic.game.lottery

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
        return records.sortedBy { it.id }
    }

    @Throws(IOException::class)
    abstract fun parse(input: InputStream): List<LotteryRecord>
}