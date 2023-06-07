package org.example.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@UtilityClass
public class CsvUtil {
    public static List<String[]> read(String source) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(source))) {
            return reader.readAll();
        }
    }
}