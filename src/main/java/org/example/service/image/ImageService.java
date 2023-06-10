package org.example.service.image;

import org.example.controller.request.GetImageRequest;
import org.example.custom_exception.NotFoundException;
import org.example.service.content.ContentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImageService {
    @Value("${folder.path.image}")
    private String imagePathFolder;

    public File get(GetImageRequest getImageRequest) {
        Map<String, List<LinkedHashMap>> imageToPartnerId = (Map<String, List<LinkedHashMap>>) ContentService.CONTENT_TO_PARTNER_ID.get("images");
        Map<String, List<LinkedHashMap>> partnerIdToLanguage = (Map<String, List<LinkedHashMap>>) imageToPartnerId.get(getImageRequest.getPartnerId());
        Map<String, List<LinkedHashMap>> languageToDate = (Map<String, List<LinkedHashMap>>) partnerIdToLanguage.get("en");
        List<LinkedHashMap> contents = languageToDate.get("19700101");
        for (LinkedHashMap content : contents) {
            String key = content.get("key").toString();
            if (!key.equals(getImageRequest.getImageKey())) {
                continue;
            }
            String absoluteFilePath = imagePathFolder + content.get("value").toString();
            File file = new File(absoluteFilePath);
            return file;
        }
        throw new NotFoundException("Can not find image file");
    }
}
