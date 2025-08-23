package com.github.pnemonic.game.lottery.lotto

import com.github.pnemonic.csv.CSVLine
import com.github.pnemonic.game.lottery.CSVLotteryResultsReader
import com.github.pnemonic.game.lottery.LotteryRecord
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class LottoResultsReader : CSVLotteryResultsReader() {
    private val format: DateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun parseLine(line: CSVLine): LotteryRecord {
        val record = LotteryRecord(NUM_BALLS)
        val columns = getCleanColumns(line)

        record.id = columns[COLUMN_SEQ].toInt()
        record.date = Calendar.getInstance().apply {
            time = format.parse(columns[COLUMN_DATE])
            set(Calendar.HOUR, 23)
        }
        var col = COLUMN_BALL
        for (i in 0 until NUM_BALLS) {
            record.balls[i] = columns[col++].toInt()
        }
        record.bonus = columns[col].toInt()
        return record
    }

    companion object {
        private const val COLUMN_SEQ = 0
        private const val COLUMN_DATE = 1
        private const val COLUMN_BALL = 2
        private const val NUM_BALLS = 6
    }
}