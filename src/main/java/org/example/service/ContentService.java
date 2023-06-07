package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.request.GetContentRequest;
import org.example.controller.request.GetImageRequest;
import org.example.custom_exception.NotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
@Slf4j
public class ContentService {
    public static Map<String, Object> RECORD_TO_PARTNER_ID = new ConcurrentHashMap<>();

    @Cacheable(value = "contents")
    public Map<String, Object> getContents(GetContentRequest getContentRequest) {
        Map<String, Object> filteredContent = filterContent(getContentRequest);
        return filteredContent;
    }

    private Map<String, Object> filterContent(GetContentRequest getContentRequest) {
        Map<String, Object> contentToPartnerId = (Map<String, Object>) RECORD_TO_PARTNER_ID.get(getContentRequest.getContentType());
        Map<String, Object> partnerIdToLanguage = (Map<String, Object>) contentToPartnerId.get(getContentRequest.getPartnerId());
        return Map.of(
                getContentRequest.getContentType(), Map.of(
                        getContentRequest.getPartnerId(), partnerIdToLanguage
                )
        );
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
