package org.example.service.csv.dagger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.request.CreateCsvDaggerRequest;
import org.example.mapper.CsvDaggerMapper;
import org.example.mapper.RecordMapper;
import org.example.model.CsvDagger;
import org.example.model.Record;
import org.example.service.SlowService;
import org.example.service.cache.CacheService;
import org.example.validator.CsvDaggerValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Service
@Slf4j
public class CsvDaggerService {
    public static Map<String, List<Record>> DATE_TO_RECORDS = new ConcurrentHashMap<>();
    private final SlowService slowService;
    private final CacheService cacheService;

    public List<CsvDagger> getAll(String fetchDate) {
        log.info("Call method to fetching csv daggers with fetch date: " + fetchDate);
        slowService.simulate(3000L);
        if (!StringUtils.isEmpty(fetchDate)) {
            CsvDaggerValidator.validateFetchDate(fetchDate);
        }
        Map<String, List<Record>> filteredDateToRecords = filterDateToRecords(fetchDate);
        Map<String, List<Record>> sortedDateToRecords = sortDateToRecords(filteredDateToRecords);
        List<CsvDagger> csvDaggers = CsvDaggerMapper.toList(sortedDateToRecords);
        return csvDaggers;
    }

    public void create(CreateCsvDaggerRequest createCsvDaggerRequest) {
        log.info("Call method to create new csv dagger");
        addRecordToMap(createCsvDaggerRequest);
    }

    private void addRecordToMap(CreateCsvDaggerRequest createCsvDaggerRequest) {
        String entryDate = createCsvDaggerRequest.getEntryDate();
        List<Record> records;
        if (DATE_TO_RECORDS.containsKey(entryDate)) {
            records = DATE_TO_RECORDS.get(entryDate);
        } else {
            records = new ArrayList<>();
        }
        Record record = RecordMapper.toModel(createCsvDaggerRequest);
        records.add(record);
        DATE_TO_RECORDS.put(entryDate, records);
    }

    public Map<String, List<Record>> filterDateToRecords(String fetchDate) {
        Map<String, List<Record>> filteredDateToRecords = new HashMap<>();
        if (StringUtils.isEmpty(fetchDate)) {
            return DATE_TO_RECORDS;
        }
        for (Map.Entry<String, List<Record>> entry : DATE_TO_RECORDS.entrySet()) {
            if (CsvDaggerValidator.isFetchDateAfterEntryDate(fetchDate, entry.getKey())) {
                continue;
            }
            filteredDateToRecords.put(entry.getKey(), entry.getValue());
        }
        return filteredDateToRecords;
    }

    public Map<String, List<Record>> sortDateToRecords(Map<String, List<Record>> filteredDateToRecords) {
        return filteredDateToRecords.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                                LinkedHashMap::new));
    }
}
