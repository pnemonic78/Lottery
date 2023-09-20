package com.github.pnemonic.game.lottery.pais123;

import com.github.pnemonic.csv.CSVFile;
import com.github.pnemonic.csv.CSVLine;
import com.github.pnemonic.game.lottery.Lottery;
import com.github.pnemonic.game.lottery.LotteryRecord;
import com.github.pnemonic.game.lottery.LotteryResultsReader;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Lotto123ResultsReader extends LotteryResultsReader {

    protected static final int COLUMN_DATE = 0;
    protected static final int COLUMN_SEQ = 1;
    protected static final int COLUMN_BALL = 4;

    protected static final int NUM_BALLS = 3;

    public Lotto123ResultsReader() {
        super();
    }

    @Override
    public List<LotteryRecord> parse(InputStream in) throws IOException {
        List<LotteryRecord> records = new ArrayList<LotteryRecord>();
        CSVFile csv = new CSVFile(in);
        Iterator<CSVLine> lines = csv.iterator();
        CSVLine line;
        String[] columns;
        Lottery lottery = new Lotto123();
        LotteryRecord record;
        Date date;
        DateFormat format = new SimpleDateFormat("dd/MM/yy");

        // ignore header
        lines.next();
        while (lines.hasNext()) {
            line = lines.next();
            record = new LotteryRecord(lottery, NUM_BALLS);
            columns = getCleanColumns(line);

            record.id = Integer.parseInt(columns[COLUMN_SEQ]);
            record.date = Calendar.getInstance();
            try {
                date = format.parse(columns[COLUMN_DATE]);
                record.date.setTime(date);
            } catch (ParseException e) {
                // ignore date
            }
            for (int l = 0, col = COLUMN_BALL; l < NUM_BALLS; l++, col--) {
                record.lot[l] = Integer.parseInt(columns[col]);
            }

            addRecord(records, record);
        }

        csv.close();

        return records;
    }

    protected void addRecord(List<LotteryRecord> records, LotteryRecord record) {
        records.add(record);
    }

    protected String[] getCleanColumns(CSVLine line) {
        String[] columns = line.getColumns();
        String col;
        for (int i = 0; i < columns.length; i++) {
            col = columns[i];
            if ((col.length() > 0) && (col.charAt(0) == '=')) {
                col = col.substring(1);
            }
            columns[i] = col;
        }
        return columns;
    }
}
