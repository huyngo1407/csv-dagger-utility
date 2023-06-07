package org.example.mapper;

import lombok.experimental.UtilityClass;
import org.example.controller.request.CreateCsvDaggerRequest;
import org.example.model.Record;

@UtilityClass
public class RecordMapper {
    public static Record toModel(CreateCsvDaggerRequest createCsvDaggerRequest) {
        String partnerId = createCsvDaggerRequest.getPartnerId();
        String language = createCsvDaggerRequest.getLanguage();
        String key = createCsvDaggerRequest.getKey();
        String value = createCsvDaggerRequest.getValue();
        return Record.builder()
                .partnerId(partnerId)
                .language(language)
                .key(key)
                .value(value)
                .build();
    }

    public static Record toModel(String[] csvLineData) {
        String partnerId = csvLineData[1];
        String language = csvLineData[2];
        String key = csvLineData[3];
        String value = csvLineData[4];
        return Record.builder()
                .partnerId(partnerId)
                .language(language)
                .key(key)
                .value(value)
                .build();
    }
}
