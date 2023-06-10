package org.example.service.content;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.request.GetContentRequest;
import org.example.mapper.ContentMapper;
import org.example.model.Content;
import org.example.validator.GetContentsValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContentService {

    public static Map<String, Object> CONTENT_TO_PARTNER_ID = new ConcurrentHashMap<>();

    //    @Cacheable(value = "contents")
    public List<Content> getContents(GetContentRequest getContentRequest) {
        log.info("Start fetching contents");
        List<Content> contents = filterContent(getContentRequest);
        return contents;
    }

    private List<Content> filterContent(GetContentRequest getContentRequest) {
        if (CONTENT_TO_PARTNER_ID.isEmpty() || !GetContentsValidator.isContentTypeValid(getContentRequest.getContentType())) {
            return new ArrayList<>();
        }
        Map<String, LinkedHashMap> filteredByContentType = (Map<String, LinkedHashMap>) CONTENT_TO_PARTNER_ID.getOrDefault(getContentRequest.getContentType(), new HashMap<>());
        Map<String, LinkedHashMap> filteredByPartnerId = (Map<String, LinkedHashMap>) filteredByContentType.getOrDefault(getContentRequest.getPartnerId(), new LinkedHashMap<>());
        Map<String, LinkedHashMap> filteredByLanguage = filterByLanguage(filteredByPartnerId, getContentRequest.getLanguage());
        Map<String, LinkedHashMap> filteredByLastFetchDate = filterByLastFetchDate(filteredByLanguage, getContentRequest.getLastFetchDate());
        Map<String, List<Content>> reconstructedAndCombinedDataToSameDate = reconstructAndCombineDataToSameDate(filteredByLastFetchDate, getContentRequest.getPartnerId());
        Map<String, List<Content>> sortedData = sortData(reconstructedAndCombinedDataToSameDate);
        List<Content> flattedData = flatData(sortedData);
        return flattedData;
    }

    private Map<String, LinkedHashMap> filterByLanguage(Map<String, LinkedHashMap> filteredByPartnerId, String language) {
        return !StringUtils.hasText(language) ?
                (LinkedHashMap) filteredByPartnerId :
                Map.of(
                        language,
                        filteredByPartnerId.get(language)
                );
    }

    private Map<String, LinkedHashMap> filterByLastFetchDate(Map<String, LinkedHashMap> filteredByLanguage, String lastFetchDate) {
        if (!StringUtils.hasText(lastFetchDate)) {
            return filteredByLanguage;
        }
        Map<String, LinkedHashMap> filteredByLastFetchDate = new HashMap<>();
        for (Map.Entry<String, LinkedHashMap> filteredByLanguageEntry : filteredByLanguage.entrySet()) {
            LinkedHashMap filteredResult = ((Map<String, LinkedHashMap>) filteredByLanguageEntry.getValue()).entrySet().stream()
                    .filter(dateToKeyValueEntry -> !GetContentsValidator.isLastFetchDateAfterEntryDate(lastFetchDate, String.valueOf(dateToKeyValueEntry.getKey())))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                                    LinkedHashMap::new));
            filteredByLastFetchDate.put(filteredByLanguageEntry.getKey(), filteredResult);
        }
        return filteredByLastFetchDate;
    }

    private Map<String, List<Content>> reconstructAndCombineDataToSameDate(Map<String, LinkedHashMap> filteredAndSortedByLastFetchDate, String partnerId) {
        Map<String, List<Content>> result = new HashMap<>();
        filteredAndSortedByLastFetchDate.entrySet().stream()
                .forEach((filteredAndSortedByLastFetchDateEntry) -> {
                    String language = filteredAndSortedByLastFetchDateEntry.getKey();
                    Map<String, LinkedHashMap> dateToKeyValue = filteredAndSortedByLastFetchDateEntry.getValue();
                    dateToKeyValue.entrySet().stream()
                            .forEach((dateToKeyValueEntry) -> {
                                String entryDate = String.valueOf(dateToKeyValueEntry.getKey());
                                if (ObjectUtils.isEmpty(result.get(entryDate))) {
                                    List<Content> contents = new ArrayList<>();
                                    ((List<LinkedHashMap>) dateToKeyValueEntry.getValue()).stream()
                                            .forEach(linkedHashMap -> contents.add(ContentMapper.toModel(linkedHashMap, partnerId, language)));
                                    result.put(entryDate, contents);
                                } else {
                                    List<Content> contents = result.get(entryDate);
                                    ((List<LinkedHashMap>) dateToKeyValueEntry.getValue()).stream()
                                            .forEach(linkedHashMap -> contents.add(ContentMapper.toModel(linkedHashMap, partnerId, language)));
                                    result.put(entryDate, contents);
                                }
                            });
                });
        return result;
    }

    private Map<String, List<Content>> sortData(Map<String, List<Content>> reconstructedAndCombinedDataToSameDate) {
        return reconstructedAndCombinedDataToSameDate.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
                                LinkedHashMap::new));
    }

    private List<Content> flatData(Map<String, List<Content>> reconstructedAndCombinedDataToSameDate) {
        List<Content> contents = new ArrayList<>();
        reconstructedAndCombinedDataToSameDate.entrySet().stream()
                .forEach(reconstructedAndCombinedDataToSameDateEntry -> {
                    List<Content> data = reconstructedAndCombinedDataToSameDateEntry.getValue();
                    contents.addAll(data);
                });
        return contents;
    }
}
