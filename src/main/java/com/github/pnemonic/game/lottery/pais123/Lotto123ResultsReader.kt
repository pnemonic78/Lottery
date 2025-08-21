package com.github.pnemonic.game.lottery.pais123

import com.github.pnemonic.csv.CSVFile
import com.github.pnemonic.csv.CSVLine
import com.github.pnemonic.game.lottery.LotteryRecord
import com.github.pnemonic.game.lottery.LotteryResultsReader
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class Lotto123ResultsReader : LotteryResultsReader() {
    @Throws(IOException::class)
    override fun parse(input: InputStream): List<LotteryRecord> {
        val records = mutableListOf<LotteryRecord>()
        val csv = CSVFile(input)
        val lines = csv.iterator()
        var line: CSVLine
        var columns: Array<String>
        var record: LotteryRecord
        val format: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val numBalls = NUM_BALLS

        // ignore header
        lines.next()
        while (lines.hasNext()) {
            line = lines.next()
            record = LotteryRecord(numBalls)
            columns = getCleanColumns(line)

            record.id = columns[COLUMN_SEQ].toInt()
            record.date = Calendar.getInstance().apply {
                time = format.parse(columns[COLUMN_DATE])
            }
            var col = COLUMN_BALL
            for (i in 0 until numBalls) {
                record.balls[i] = columns[col++].toInt()
            }
            records.add(record)
        }
        csv.close()
        return records
    }

    companion object {
        private const val COLUMN_DATE = 0
        private const val COLUMN_SEQ = 1
        private const val COLUMN_BALL = 2
        private const val NUM_BALLS = 3
    }
}