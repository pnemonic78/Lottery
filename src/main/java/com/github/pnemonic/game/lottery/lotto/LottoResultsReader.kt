package com.github.pnemonic.game.lottery.lotto

import com.github.pnemonic.csv.CSVFile
import com.github.pnemonic.csv.CSVLine
import com.github.pnemonic.game.lottery.LotteryRecord
import com.github.pnemonic.game.lottery.LotteryResultsReader
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class LottoResultsReader : LotteryResultsReader() {
    @Throws(IOException::class)
    override fun parse(input: InputStream): List<LotteryRecord> {
        val records = mutableListOf<LotteryRecord>()
        val csv = CSVFile(input)
        val lines = csv.iterator()
        var line: CSVLine
        var columns: Array<String>
        var record: LotteryRecord
        val format: DateFormat = SimpleDateFormat("dd/MM/yyyy")

        // ignore header
        lines.next()
        while (lines.hasNext()) {
            line = lines.next()
            record = LotteryRecord(NUM_BALLS)
            columns = getCleanColumns(line)

            record.id = columns[COLUMN_SEQ].toInt()
            record.date = Calendar.getInstance().apply {
                time = format.parse(columns[COLUMN_DATE])
                set(Calendar.HOUR, 23)
            }
            var col = COLUMN_BALL
            for (l in 0 until NUM_BALLS) {
                record.balls[l] = columns[col++].toInt()
            }
            record.bonus = columns[col].toInt()
//            if (record.balls[5] > 37) {
//                break
//            }
//            if (record.bonus > 10) {
//                break
//            }
            addRecord(records, record)
        }
        csv.close()
        return records.sortedBy { it.id }
    }

    private fun addRecord(records: MutableList<LotteryRecord>, record: LotteryRecord) {
        records.add(record)
    }

    private fun getCleanColumns(line: CSVLine): Array<String> {
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
        private const val COLUMN_SEQ = 0
        private const val COLUMN_DATE = 1
        private const val COLUMN_BALL = 2
        private const val NUM_BALLS = 6
    }
}