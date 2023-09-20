package com.github.pnemonic.game.lottery.pais777

import com.github.pnemonic.csv.CSVFile
import com.github.pnemonic.csv.CSVLine
import com.github.pnemonic.game.lottery.Lottery
import com.github.pnemonic.game.lottery.LotteryRecord
import com.github.pnemonic.game.lottery.LotteryResultsReader
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class Lotto777ResultsReader : LotteryResultsReader() {
    @Throws(IOException::class)
    override fun parse(input: InputStream): List<LotteryRecord> {
        val records: MutableList<LotteryRecord> = ArrayList()
        val csv = CSVFile(input)
        val lines = csv.iterator()
        var line: CSVLine
        var columns: Array<String>
        val lottery: Lottery = Lotto777()
        var record: LotteryRecord
        val format: DateFormat = SimpleDateFormat("dd/MM/yy")

        // ignore header
        lines.next()
        while (lines.hasNext()) {
            line = lines.next()
            record = LotteryRecord(lottery, NUM_BALLS)
            columns = getCleanColumns(line)

            record.id = columns[COLUMN_SEQ].toInt()
            record.date = Calendar.getInstance()
            try {
                record.date!!.time = format.parse(columns[COLUMN_DATE])
            } catch (e: ParseException) {
                // ignore date
            }
            var col = COLUMN_BALL
            for (l in 0 until  NUM_BALLS) {
                record.lot[l] = columns[col++].toInt()
            }
            addRecord(records, record)
        }
        csv.close()
        return records
    }

    protected fun addRecord(records: MutableList<LotteryRecord>, record: LotteryRecord) {
        records.add(record)
    }

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

    companion object {
        private const val COLUMN_DATE = 0
        private const val COLUMN_SEQ = 1
        private const val COLUMN_BALL = 2
        private const val NUM_BALLS = 17
    }
}