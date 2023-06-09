package org.example.service.content;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.request.ContentRequest;
import org.example.controller.request.GetImageRequest;
import org.example.custom_exception.NotFoundException;
import org.example.model.KeyValue;
import org.example.validator.ContentsValidator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContentService {
    public static Map<String, Object> RECORD_TO_PARTNER_ID = new ConcurrentHashMap<>();


    @Cacheable(value = "contents")
    public List<KeyValue> getContents(ContentRequest contentRequest) {
        log.info("Start fetching contents");
        List<KeyValue> keyValues = filterContent(contentRequest);
        return keyValues;
    }

    private List<KeyValue> filterContent(ContentRequest contentRequest) {
        if (RECORD_TO_PARTNER_ID.isEmpty() || !ContentsValidator.isContentTypeValid(contentRequest.getContentType())) {
            return new ArrayList<>();
        }
        Map<String, List<LinkedHashMap>> partnerIdToLanguage = (Map<String, List<LinkedHashMap>>) RECORD_TO_PARTNER_ID.get(contentRequest.getContentType());
        Map<String, List<LinkedHashMap>> languageToDate = (Map<String, List<LinkedHashMap>>) partnerIdToLanguage.get(contentRequest.getPartnerId());
        List<Map<String, List<LinkedHashMap>>> dateToKeyValues = !ObjectUtils.isEmpty(languageToDate.get(contentRequest.getLang())) ?
                Collections.singletonList((Map<String, List<LinkedHashMap>>) languageToDate.get(contentRequest.getLang())) :
                languageToDate.entrySet()
                        .stream().map(languageToDateEntry -> (Map<String, List<LinkedHashMap>>) languageToDateEntry.getValue())
                        .collect(Collectors.toList());
        Map<String, List<KeyValue>> combinedDateToKeyValue = combineDateToKeyValue(dateToKeyValues);
        Map<String, List<KeyValue>> filteredDateToKeyValue = filterDateToKeyValue(combinedDateToKeyValue, contentRequest.getLastFetchDate());
        Map<String, List<KeyValue>> sortedDateToKeyValue = sortDateToKeyValue(filteredDateToKeyValue);
        return getKeyValues(sortedDateToKeyValue);
    }

    private Map<String, List<KeyValue>> combineDateToKeyValue(List<Map<String, List<LinkedHashMap>>> dateToKeyValues) {
        Map<String, List<KeyValue>> combinedDateToKeyValue = new HashMap<>();
        for (Map<String, List<LinkedHashMap>> dateToKeyValue : dateToKeyValues) {
            for (Map.Entry<String, List<LinkedHashMap>> dateToKeyValueEntry : dateToKeyValue.entrySet()) {
                String entryDate = String.valueOf(dateToKeyValueEntry.getKey());
                if (ObjectUtils.isEmpty(combinedDateToKeyValue.get(entryDate))) {
                    List<KeyValue> keyValues = new ArrayList<>();
                    dateToKeyValueEntry.getValue().stream()
                            .forEach(linkedHashMap -> keyValues.add(KeyValue.builder()
                                    .key(String.valueOf(linkedHashMap.get("key")))
                                    .value(String.valueOf(linkedHashMap.get("value")))
                                    .build()));
                    combinedDateToKeyValue.put(entryDate, keyValues);
                } else {
                    List<KeyValue> keyValues = combinedDateToKeyValue.get(entryDate);
                    dateToKeyValueEntry.getValue().stream()
                            .forEach(linkedHashMap -> keyValues.add(KeyValue.builder()
                                    .key(String.valueOf(linkedHashMap.get("key")))
                                    .value(String.valueOf(linkedHashMap.get("value")))
                                    .build()));
                }
            }
        }
        return combinedDateToKeyValue;
    }

    private Map<String, List<KeyValue>> filterDateToKeyValue(Map<String, List<KeyValue>> dateToKeyValue, String lastFetchDate) {
        if (!StringUtils.hasText(lastFetchDate)) {
            return dateToKeyValue;
        }
        return dateToKeyValue.entrySet().stream()
                .filter(dateToKeyValueEntry -> !ContentsValidator.isLastFetchDateAfterEntryDate(lastFetchDate, String.valueOf(dateToKeyValueEntry.getKey())))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                                LinkedHashMap::new));
    }

    private Map<String, List<KeyValue>> sortDateToKeyValue(Map<String, List<KeyValue>> dateToKeyValue) {
        return dateToKeyValue.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                                LinkedHashMap::new));
    }

    private List<KeyValue> getKeyValues(Map<String, List<KeyValue>> dateToKeyValue) {
        List<KeyValue> keyValues = new ArrayList<>();
        dateToKeyValue.entrySet().stream()
                .forEach(sortedDateToKeyValueEntry -> sortedDateToKeyValueEntry.getValue().stream()
                        .forEach(keyValue -> keyValues.add(keyValue)));
        return keyValues;
    }

    public File getImage(GetImageRequest getImageRequest) {
        Map<String, List<LinkedHashMap>> imageToPartnerId = (Map<String, List<LinkedHashMap>>) RECORD_TO_PARTNER_ID.get("image");
        Map<String, List<LinkedHashMap>> partnerIdToLanguage = (Map<String, List<LinkedHashMap>>) imageToPartnerId.get(getImageRequest.getPartnerId());
        Map<String, List<LinkedHashMap>> languageToDate = (Map<String, List<LinkedHashMap>>) partnerIdToLanguage.get("en");
        List<LinkedHashMap> keyValues = languageToDate.get("19700101");
        for (LinkedHashMap keyValue : keyValues) {
            String key = keyValue.get("key").toString();
            if (!key.equals(getImageRequest.getImageKey())) {
                continue;
            }
            String absoluteFilePath = keyValue.get("value").toString();
            File file = new File(absoluteFilePath);
            return file;
        }
        throw new NotFoundException("Can not find image file");
    }
}
