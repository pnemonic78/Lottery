package com.github.pnemonic.game.lottery.lotto;

import com.github.pnemonic.csv.CSVFile;
import com.github.pnemonic.csv.CSVLine;
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

public class LottoResultsReader extends LotteryResultsReader {

    public LottoResultsReader() {
        super();
    }

    @Override
    public List<LotteryRecord> parse(InputStream in) throws IOException {
        List<LotteryRecord> records = new ArrayList<LotteryRecord>();
        CSVFile csv = new CSVFile(in);
        Iterator<CSVLine> lines = csv.iterator();
        CSVLine line;
        String[] columns;
        LotteryRecord record;
        Date date;
        DateFormat format = new SimpleDateFormat("dd/MM/yy");

        // ignore header
        lines.next();
        while (lines.hasNext()) {
            line = lines.next();
            record = new LotteryRecord(6);
            columns = getCleanColumns(line);

            record.id = Integer.parseInt(columns[0]);
            record.date = Calendar.getInstance();
            try {
                date = format.parse(columns[1]);
                record.date.setTime(date);
            } catch (ParseException e) {
                // ignore date
            }
            record.date.set(Calendar.HOUR, 23);
            record.lot[0] = Integer.parseInt(columns[2]);
            record.lot[1] = Integer.parseInt(columns[3]);
            record.lot[2] = Integer.parseInt(columns[4]);
            record.lot[3] = Integer.parseInt(columns[5]);
            record.lot[4] = Integer.parseInt(columns[6]);
            record.lot[5] = Integer.parseInt(columns[7]);
            record.bonus = Integer.parseInt(columns[8]);

            if (record.lot[5] > 37) {
                break;
            }
            if (record.bonus > 10) {
                break;
            }
            addRecord(records, record);
        }

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
