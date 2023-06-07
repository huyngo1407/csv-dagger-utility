package org.example.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ContentMapper {
    public static Map<String, Object> toMap(Map<String, Object> imageData, Map<String, Object> contentData) throws JsonProcessingException {
        Map<String, Object> labelToPartnerId = getLabels(contentData);
        Map<String, Object> errorToPartnerId = getErrors(contentData);
        Map<String, Object> imageToPartnerId = getImages(imageData);
        Map<String, Object> result = new HashMap<>();
        result.put("label", labelToPartnerId);
        result.put("error", errorToPartnerId);
        result.put("image", imageToPartnerId);
        return result;
    }

    private static Map<String, Object> getLabels(Map<String, Object> contentData) {
        LinkedHashMap<String, Object> labelData = (LinkedHashMap<String, Object>) contentData.get("label");
        return labelData;
    }

    private static Map<String, Object> getErrors(Map<String, Object> contentData) {
        LinkedHashMap<String, Object> errorData = (LinkedHashMap<String, Object>) contentData.get("error");
        return errorData;
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
