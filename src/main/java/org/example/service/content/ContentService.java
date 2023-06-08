package org.example.service.content;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.request.GetContentRequest;
import org.example.controller.request.GetImageRequest;
import org.example.custom_exception.NotFoundException;
import org.example.model.KeyValue;
import org.example.validator.GetContentsValidator;
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
    public List<KeyValue> getContents(GetContentRequest getContentRequest) {
        log.info("Start fetching contents");
        List<KeyValue> keyValues = filterContent(getContentRequest);
        return keyValues;
    }

    private List<KeyValue> filterContent(GetContentRequest getContentRequest) {
        if (RECORD_TO_PARTNER_ID.isEmpty() || !GetContentsValidator.isContentTypeValid(getContentRequest.getContentType())) {
            return new ArrayList<>();
        }
        Map<String, Object> partnerIdToLanguage = (Map<String, Object>) RECORD_TO_PARTNER_ID.get(getContentRequest.getContentType());
        Map<String, Object> languageToDate = (Map<String, Object>) partnerIdToLanguage.get(getContentRequest.getPartnerId());
        Map<String, Object> dateToKeyValue = StringUtils.hasText(getContentRequest.getLang())
                ? ObjectUtils.isEmpty(languageToDate.get(getContentRequest.getLang()))
                ? new HashMap<>()
                : (Map<String, Object>) languageToDate.get(getContentRequest.getLang())
                : new HashMap<>();
        Map<String, Object> filteredDateToKeyValue = filterDateToKeyValue(dateToKeyValue, getContentRequest.getLastFetchDate());
        Map<String, Object> sortedDateToKeyValue = sortDateToKeyValue(filteredDateToKeyValue);
        List<KeyValue> result = new ArrayList<>();
        for (Map.Entry<String, Object> sortedDateToKeyValueEntry : sortedDateToKeyValue.entrySet()) {
            List<KeyValue> keyValues = ((List<LinkedHashMap>) sortedDateToKeyValueEntry.getValue()).stream()
                    .map(linkedHashMap -> KeyValue.builder()
                            .key(linkedHashMap.get("key").toString())
                            .value(linkedHashMap.get("value").toString())
                            .build()
                    ).collect(Collectors.toList());
            result.addAll(keyValues);
        }
        return result;
    }

    private Map<String, Object> filterDateToKeyValue(Map<String, Object> dateToKeyValue, String lastFetchDate) {
        Map<String, Object> filteredDateToKeyValue = new HashMap<>();
        if (!StringUtils.hasText(lastFetchDate)) {
            filteredDateToKeyValue = dateToKeyValue;
        }
        for (Map.Entry<String, Object> entry : dateToKeyValue.entrySet()) {
            String entryDate = String.valueOf(entry.getKey());
            if (GetContentsValidator.isLastFetchDateAfterEntryDate(lastFetchDate, entryDate)) {
                continue;
            }
            filteredDateToKeyValue.put(entryDate, entry.getValue());
        }
        return filteredDateToKeyValue;
    }

    private Map<String, Object> sortDateToKeyValue(Map<String, Object> dateToKeyValue) {
        return dateToKeyValue.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                                LinkedHashMap::new));
    }

    public File getImage(GetImageRequest getImageRequest) {
        Map<String, Object> imageToPartnerId = (Map<String, Object>) RECORD_TO_PARTNER_ID.get("image");
        Map<String, Object> partnerIdToLanguage = (Map<String, Object>) imageToPartnerId.get(getImageRequest.getPartnerId());
        Map<String, Object> languageToDate = (Map<String, Object>) partnerIdToLanguage.get("en");
        List<LinkedHashMap> keyValues = (List<LinkedHashMap>) languageToDate.get("19700101");
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
