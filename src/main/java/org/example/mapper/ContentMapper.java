package org.example.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.experimental.UtilityClass;
import org.example.model.Content;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ContentMapper {
    public static Content toModel(LinkedHashMap linkedHashMap, String partnerId, String language) {
        return Content.builder()
                .version(String.valueOf(linkedHashMap.get("version")))
                .partnerId(partnerId)
                .language(language)
                .key(String.valueOf(linkedHashMap.get("key")))
                .value(String.valueOf(linkedHashMap.get("value")))
                .build();
    }


    public static Map<String, Object> toMap(Map<String, Object> imageData, Map<String, Object> contentData) throws JsonProcessingException {
        Map<String, Object> labelToPartnerId = (LinkedHashMap<String, Object>) contentData.get("labels");
        Map<String, Object> errorToPartnerId = (LinkedHashMap<String, Object>) contentData.get("errors");
        Map<String, Object> imageToPartnerId = getImages(imageData);
        Map<String, Object> result = new HashMap<>();
        result.put("labels", labelToPartnerId);
        result.put("errors", errorToPartnerId);
        result.put("images", imageToPartnerId);
        return result;
    }

    private static Map<String, Object> getImages(Map<String, Object> imageData) {
        Map<String, Object> partnerIdToLanguage = new HashMap<>();
        for (Map.Entry<String, Object> entry : imageData.entrySet()) {
            String partnerId = entry.getKey();
            List<LinkedHashMap<String, Object>> values = (List<LinkedHashMap<String, Object>>) entry.getValue();
            Map<String, Object> dateToEnKeyValue = new HashMap<>();
            dateToEnKeyValue.put("19700101", values);
            Map<String, Object> langToDate = new HashMap<>();
            langToDate.put("en", dateToEnKeyValue);
            partnerIdToLanguage.put(partnerId, langToDate);
        }
        return partnerIdToLanguage;
    }
}
