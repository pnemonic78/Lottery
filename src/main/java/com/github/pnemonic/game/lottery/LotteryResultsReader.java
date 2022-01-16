package com.github.pnemonic.game.lottery;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public abstract class LotteryResultsReader {

	public LotteryResultsReader() {
		super();
	}

	public List<LotteryRecord> parse(File file) throws IOException {
		List<LotteryRecord> records;
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			records = parse(in);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					// consume
				}
			}
		}
		Collections.sort(records);
		return records;
	}

	public abstract List<LotteryRecord> parse(InputStream in) throws IOException;

}
