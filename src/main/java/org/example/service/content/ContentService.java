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
        List<Map<String, List<LinkedHashMap>>> filteredDateToKeyValues = filterDateToKeyValues(dateToKeyValues, contentRequest.getLastFetchDate());
        List<Map<String, List<LinkedHashMap>>> sortedDateToKeyValues = sortDateToKeyValues(filteredDateToKeyValues);
        List<KeyValue> keyValues = new ArrayList<>();
        sortedDateToKeyValues.stream()
                .forEach(sortedDateToKeyValue -> sortedDateToKeyValue.entrySet().stream()
                        .forEach(sortedDateToKeyValueEntry -> sortedDateToKeyValueEntry.getValue().stream()
                                .forEach(keyValue -> keyValues.add(KeyValue.builder()
                                        .key(String.valueOf(keyValue.get("key")))
                                        .value(String.valueOf(keyValue.get("value")))
                                        .build()))));

        return keyValues;
    }

    private List<Map<String, List<LinkedHashMap>>> filterDateToKeyValues(List<Map<String, List<LinkedHashMap>>> dateToKeyValues, String lastFetchDate) {
        if (!StringUtils.hasText(lastFetchDate)) {
            return dateToKeyValues;
        }
        return dateToKeyValues.stream()
                .map(dateToKeyValue -> dateToKeyValue.entrySet().stream()
                        .filter(dateToKeyValueEntry -> !ContentsValidator.isLastFetchDateAfterEntryDate(lastFetchDate, String.valueOf(dateToKeyValueEntry.getKey())))
                        .collect(
                                toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                                        LinkedHashMap::new)))
                .collect(Collectors.toList());
    }

    private List<Map<String, List<LinkedHashMap>>> sortDateToKeyValues(List<Map<String, List<LinkedHashMap>>> dateToKeyValues) {
        return dateToKeyValues.stream()
                .map(dateToKeyValue -> dateToKeyValue.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey())
                        .collect(
                                toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                                        LinkedHashMap::new)))
                .collect(Collectors.toList());
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
