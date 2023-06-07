package org.example.mapper;


import lombok.experimental.UtilityClass;
import org.example.model.CsvDagger;
import org.example.model.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class CsvDaggerMapper {
    public static List<CsvDagger> toList(Map<String, List<Record>> csvDaggerMaps) {
        List<CsvDagger> csvDaggers = new ArrayList<>();
        for (Map.Entry<String, List<Record>> entry : csvDaggerMaps.entrySet()) {
            CsvDagger csvDagger = toModel(entry);
            csvDaggers.add(csvDagger);
        }
        return csvDaggers;
    }

    public static Map<String, List<Record>> toMap(List<String[]> csvData) {
        Map<String, List<Record>> dateToRecords = new HashMap<>();
        for (String[] csvLineData : csvData) {
            String entryDate = csvLineData[0];
            List<Record> records;
            if (dateToRecords.containsKey(entryDate)) {
                records = dateToRecords.get(entryDate);
            } else {
                records = new ArrayList<>();
            }
            records.add(RecordMapper.toModel(csvLineData));
            dateToRecords.put(entryDate, records);
        }
        return dateToRecords;
    }

    public static CsvDagger toModel(Map.Entry<String, List<Record>> entry) {
        List<Record> records = entry.getValue();

        return CsvDagger.builder()
                .records(records)
                .build();
    }
}
