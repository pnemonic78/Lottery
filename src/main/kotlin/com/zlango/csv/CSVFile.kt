package com.zlango.csv

import com.opencsv.CSVReader
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class CSVFile(inputStream: InputStream) : Iterable<CSVLine> {

    private val reader = BufferedReader(InputStreamReader(inputStream))
    private val csvReader = CSVReader(reader)

    override fun iterator(): Iterator<CSVLine> {
        return object : Iterator<CSVLine> {
            private val csvIterator = csvReader.iterator()

            override fun hasNext(): Boolean {
                return csvIterator.hasNext()
            }

            override fun next(): CSVLine {
                val columns = csvIterator.next()
                return CSVLine(columns)
            }
        }
    }

    fun close() {
        csvReader.close()
    }
}