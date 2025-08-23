package com.github.pnemonic.game.lottery

import com.github.pnemonic.csv.CSVFile
import com.github.pnemonic.csv.CSVLine
import java.io.InputStream

abstract class CSVLotteryResultsReader(private val ignoreHeader: Boolean = true) :
    LotteryResultsReader() {

    override fun parse(input: InputStream): List<LotteryRecord> {
        val records = mutableListOf<LotteryRecord>()
        val csv = CSVFile(input)
        val lines = csv.iterator()
        var line: CSVLine
        var record: LotteryRecord

        // ignore header?
        if (ignoreHeader) lines.next()
        while (lines.hasNext()) {
            line = lines.next()
            record = parseLine(line)
            records.add(record)
        }
        csv.close()
        return records
    }

    override fun parse(input: InputStream, visitor: RecordCallback) {
        val csv = CSVFile(input)
        val lines = csv.iterator()
        var line: CSVLine
        var record: LotteryRecord

        // ignore header?
        if (ignoreHeader) lines.next()
        while (lines.hasNext()) {
            line = lines.next()
            record = parseLine(line)
            visitor.invoke(record)
        }
        csv.close()
    }

    abstract fun parseLine(line: CSVLine): LotteryRecord

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